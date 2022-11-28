package directory.mastodoninstances.backend.service.mastodon

import directory.mastodoninstances.backend.clients.mastodonclient.MastodonClient
import java.time.Duration

class DefaultMastodonService(private val mastodonClient: MastodonClient) : MastodonService {
    override fun getInstanceInfo(uri: String): MastodonInstanceInfo {
        try {
            val result = mastodonClient.getInstance(uri).block(Duration.ofSeconds(5))
            return MastodonInstanceInfo().apply {
                title = result?.title
                shortDescription = result?.shortDescription
                description = result?.description
                version = result?.version
                thumbnail = result?.thumbnail
                registrations = result?.registrations
                approvalRequired = result?.approvalRequired
                invitesEnabled = result?.invitesEnabled
                languages = result?.languages?.toMutableList()
                userCount = result?.stats?.userCount
                statusCount = result?.stats?.statusCount
                domainCount = result?.stats?.domainCount
            }
        } catch (e: RuntimeException) {
            val errorMessage = "Unable to perform getInstanceInfo API call for uri $uri"
            throw MastodonServiceException(errorMessage, e)
        }
    }
}
