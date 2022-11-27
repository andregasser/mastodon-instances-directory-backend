package directory.mastodoninstances.backend.service.check

import directory.mastodoninstances.backend.persistence.model.Instance
import directory.mastodoninstances.backend.persistence.model.InstanceHousekeeping
import directory.mastodoninstances.backend.persistence.model.InstanceInfo
import directory.mastodoninstances.backend.persistence.model.InstanceLocation
import directory.mastodoninstances.backend.persistence.model.InstanceSecurity
import directory.mastodoninstances.backend.persistence.model.LastCheckResult
import directory.mastodoninstances.backend.service.geoiplookup.GeoIpLookupException
import directory.mastodoninstances.backend.service.geoiplookup.GeoIpLookupService
import directory.mastodoninstances.backend.service.instance.InstanceService
import directory.mastodoninstances.backend.service.instance.InstanceServiceException
import directory.mastodoninstances.backend.service.mastodon.MastodonService
import directory.mastodoninstances.backend.service.mastodon.MastodonServiceException
import directory.mastodoninstances.backend.service.security.SecurityService
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.Instant

class DefaultCheckService(
    private val instanceService: InstanceService,
    private val mastodonService: MastodonService,
    private val geoIpLookupService: GeoIpLookupService,
    private val securityService: SecurityService
) : CheckService {
    private val log: Logger = LoggerFactory.getLogger(DefaultCheckService::class.java)

    override fun uriExists(uri: String): Boolean =
        try {
            instanceService.uriExists(uri)
        } catch (e: Exception) {
            val errorMessage = "Unable to check if uri $uri exists"
            log.error(errorMessage, e)
            throw CheckServiceException(errorMessage, e)
        }

    override fun addUri(uri: String) {
        val instanceInfo = try {
            val mastodonInstanceInfo = mastodonService.getInstanceInfo(uri)
            InstanceInfo().apply {
                title = mastodonInstanceInfo.title  // TODO: Add uri before
                shortDescription = mastodonInstanceInfo.shortDescription
                description = mastodonInstanceInfo.description
                version = mastodonInstanceInfo.version
                thumbnail = mastodonInstanceInfo.thumbnail
                registrations = mastodonInstanceInfo.registrations
                approvalRequired = mastodonInstanceInfo.approvalRequired
                invitesEnabled = mastodonInstanceInfo.invitesEnabled
                languages = mastodonInstanceInfo.languages
                userCount = mastodonInstanceInfo.userCount
                statusCount = mastodonInstanceInfo.statusCount
                domainCount = mastodonInstanceInfo.domainCount
                housekeeping = InstanceHousekeeping().apply {
                    lastCheckResult = LastCheckResult.SUCCESS
                    lastCheckFailureReason = null
                    lastCheck = Instant.now()
                    nextCheck = Instant.now().plus(Duration.ofDays(2))  // TODO: Add random offset to distribute load
                }
            }
        } catch (e: MastodonServiceException) {
            val errorMessage = "Unable to fetch instance info for uri $uri"
            if (log.isDebugEnabled) log.debug(errorMessage, e) else log.error(errorMessage)

            InstanceInfo().apply {
                housekeeping = InstanceHousekeeping().apply {
                    lastCheckResult = LastCheckResult.FAILURE
                    lastCheckFailureReason = e.message
                    lastCheck = Instant.now()
                    nextCheck = Instant.now().plus(Duration.ofDays(5))  // TODO: Add random offset to distribute load
                }
            }
        }

        val instanceLocation = try {
            val geoIpLookupInfo = geoIpLookupService.performLookup(uri)
            InstanceLocation().apply {
                continent = geoIpLookupInfo.continent
                country = geoIpLookupInfo.country
                countryCode = geoIpLookupInfo.countryCode
                region = geoIpLookupInfo.region
                city = geoIpLookupInfo.city
                housekeeping = InstanceHousekeeping().apply {
                    lastCheckResult = LastCheckResult.SUCCESS
                    lastCheckFailureReason = null
                    lastCheck = Instant.now()
                    nextCheck = Instant.now().plus(Duration.ofDays(2))  // TODO: Add random offset to distribute load
                }
            }
        } catch (e: GeoIpLookupException) {
            val errorMessage = "Unable to fetch location info for uri $uri"
            if (log.isDebugEnabled) log.debug(errorMessage, e) else log.error(errorMessage)
            InstanceLocation().apply {
                housekeeping = InstanceHousekeeping().apply {
                    lastCheckResult = LastCheckResult.FAILURE
                    lastCheckFailureReason = e.message
                    lastCheck = Instant.now()
                    nextCheck = Instant.now().plus(Duration.ofDays(5))  // TODO: Add random offset to distribute load
                }
            }
        }

        val instance = Instance().apply {
            id = ObjectId()
            this.uri = uri
            info = instanceInfo
            location = instanceLocation
            security = InstanceSecurity()
        }

        try {
            instanceService.createInstance(instance)
            log.info("New instance added: $uri")
        } catch (e: InstanceServiceException) {
            val errorMessage = "Unable to persist data in database for uri $uri"
            if (log.isDebugEnabled) log.debug(errorMessage, e) else log.error(errorMessage)
        }
    }
}
