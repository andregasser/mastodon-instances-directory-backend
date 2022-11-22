package directory.mastodoninstances.backend.jobs

import com.google.gson.Gson
import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.exception.Mastodon4jRequestException
import com.sys1yagi.mastodon4j.api.method.Public
import directory.mastodoninstances.backend.integration.database.InstanceRepository
import directory.mastodoninstances.backend.integration.database.InstanceStatsRepository
import directory.mastodoninstances.backend.integration.database.model.Instance
import directory.mastodoninstances.backend.integration.database.model.InstanceHousekeeping
import directory.mastodoninstances.backend.integration.database.model.InstanceLocation
import directory.mastodoninstances.backend.integration.database.model.InstanceStats
import directory.mastodoninstances.backend.integration.database.model.LastCheckResult
import directory.mastodoninstances.backend.integration.geolocation.GeoIpClient
import okhttp3.OkHttpClient
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.ExchangeTypes
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import java.net.InetAddress
import java.net.UnknownHostException
import java.time.Duration
import java.time.Instant

class CheckQueueProcessor(
    private val instanceRepository: InstanceRepository,
    private val instanceStatsRepository: InstanceStatsRepository,
    private val geoIpClient: GeoIpClient,
) {

    private val log: Logger = LoggerFactory.getLogger(CheckQueueProcessor::class.java)

    @RabbitListener(
        bindings = [QueueBinding(
            value = Queue(value = "\${rabbitmq.checkqueue.name}", durable = "false", autoDelete = "false"),
            exchange = Exchange(value = "\${rabbitmq.exchange.name}", type = ExchangeTypes.DIRECT, ignoreDeclarationExceptions = "true"),
            key = arrayOf("\${rabbitmq.checkqueue.routingkey}")
        )]
    )
    fun processMessage(message: String) {
        log.debug("Processing new Mastodon instance $message")
        val instance = Instance()
        instance.id = ObjectId()
        instance.uri = message

        if (!instanceRepository.exists(message)) {
            val ip = try {
                getIpFromHostname(message)
            } catch (e: UnknownHostException) {
                val errorMessage = "Failed to resolve Ip address of hostname $message"
                log.error(errorMessage)
                instance.housekeeping = getInstanceHousekeepingDetails(LastCheckResult.FAILURE, errorMessage, Duration.ofDays(2))
                instanceRepository.create(instance)
                return
            }

            try {
                val geoIpResult = geoIpClient.getGeoIpInfoByIp(ip).block(Duration.ofSeconds(10))
                if (geoIpResult != null) {
                    val instanceLocation = InstanceLocation().apply {
                        continent = geoIpResult.continent
                        countryCode = geoIpResult.country_code
                        countryName = geoIpResult.country
                        region = geoIpResult.region
                        city = geoIpResult.city
                    }
                    instance.location = instanceLocation
                } else {
                    val errorMessage = "Failed to perform geolocation lookup for Mastodon instance $message (ip: $ip)"
                    log.error(errorMessage)
                    instance.housekeeping = getInstanceHousekeepingDetails(LastCheckResult.FAILURE, errorMessage, Duration.ofDays(2))
                    instanceRepository.create(instance)
                    return
                }
            } catch (e: RuntimeException) {
                log.error(e.message)
            }

            val instanceResult = try {
                val mastodonClient = MastodonClient.Builder(message, OkHttpClient.Builder(), Gson()).build()
                Public(mastodonClient).getInstance().execute()
            } catch (e: Mastodon4jRequestException) {
                val errorMessage = "Unable to query Mastodon instance $message. Error: ${e.message}"
                log.error(errorMessage)
                instance.housekeeping = getInstanceHousekeepingDetails(LastCheckResult.FAILURE, errorMessage, Duration.ofDays(2))
                instanceRepository.create(instance)
                return
            }

            instance.apply {
                title = instanceResult.title
                shortDescription = instanceResult.shortDescription
                description = instanceResult.description
                version = instanceResult.version
                thumbnail = instanceResult.thumbnail
                registrations = instanceResult.registrations
                approvalRequired = instanceResult.approvalRequired
                invitesEnabled = instanceResult.invitesEnabled
                languages = instanceResult.languages.toMutableList()
            }

            instance.housekeeping = getInstanceHousekeepingDetails(LastCheckResult.SUCCESS, null, Duration.ofDays(1))
            instanceRepository.create(instance)

            instanceResult.stats?.let {
                val instanceStats = InstanceStats(
                    id = ObjectId(),
                    instanceId = instance.id,
                    timestamp = Instant.now(),
                    userCount = it.userCount,
                    statusCount = it.statusCount,
                    domainCount = it.domainCount,
                )
                instanceStatsRepository.create(instanceStats)
            }

            log.info("Instance $message added")
        } else {
            log.warn("Instance $message skipped: Instance already present")
        }
    }

    private fun getInstanceHousekeepingDetails(checkResult: LastCheckResult, errorMessage: String?, nextCheckDuration: Duration): InstanceHousekeeping {
        val now = Instant.now()
        return InstanceHousekeeping().apply {
            lastCheckResult = checkResult
            lastCheckFailureReason = errorMessage
            lastCheck = now
            nextCheck = now.plus(nextCheckDuration)
        }
    }

    private fun getIpFromHostname(hostname: String): String {
        val host = InetAddress.getByName(hostname)
        return host.hostAddress
    }
}
