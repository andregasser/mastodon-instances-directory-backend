package directory.mastodoninstances.backend.service.mastodon

import com.google.gson.Gson
import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.exception.Mastodon4jRequestException
import com.sys1yagi.mastodon4j.api.method.Public
import okhttp3.OkHttpClient

class DefaultMastodonService : MastodonService {
    override fun getInstanceInfo(uri: String): MastodonInstanceInfo {
        try {
            val mastodonClient = MastodonClient.Builder(uri, OkHttpClient.Builder(), Gson()).build()
            val result = Public(mastodonClient).getInstance().execute()
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
        } catch (e: Mastodon4jRequestException) {
            val errorMessage = "Unable to perform getInstanceInfo API call for uri $uri"
            throw MastodonServiceException(errorMessage, e)
        }
    }
}
