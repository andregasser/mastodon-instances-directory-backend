package directory.mastodoninstances.backend.service.check

interface CheckService {
    fun uriExists(uri: String): Boolean
    fun addUri(uri: String)
}
