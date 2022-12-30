package directory.mastodoninstances.backend.service.mastodon

import social.bigbone.MastodonClient
import social.bigbone.api.method.Public

class DefaultMastodonService : MastodonService {
    override fun getInstanceInfo(uri: String): MastodonInstanceInfo {
        try {
            val client: MastodonClient = MastodonClient.Builder(uri).build()
            val public = Public(client)
            val result = public.getInstance().execute()
            return MastodonInstanceInfo().apply {
                title = result.title
                shortDescription = result.shortDescription
                description = result.description
                version = result.version
                thumbnail = result.thumbnail
                registrations = result.registrations
                approvalRequired = result.approvalRequired
                invitesEnabled = result.invitesEnabled
                languages = result.languages.toMutableList()
                userCount = result.stats?.userCount
                statusCount = result.stats?.statusCount
                domainCount = result.stats?.domainCount
            }
        } catch (e: RuntimeException) {
            val errorMessage = "Unable to perform getInstanceInfo API call for uri $uri"
            throw MastodonServiceException(errorMessage, e)
        }
    }
}
