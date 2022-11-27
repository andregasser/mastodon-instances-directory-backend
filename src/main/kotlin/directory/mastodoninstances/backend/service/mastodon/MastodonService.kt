package directory.mastodoninstances.backend.service.mastodon

interface MastodonService {
    fun getInstanceInfo(uri: String): MastodonInstanceInfo
}
