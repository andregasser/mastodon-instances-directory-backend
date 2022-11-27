package directory.mastodoninstances.backend.service.mastodon

data class MastodonInstanceInfo(
    var title: String? = null,
    var shortDescription: String? = null,
    var description: String? = null,
    var version: String? = null,
    var thumbnail: String? = null,
    var registrations: Boolean? = null,
    var approvalRequired: Boolean? = null,
    var invitesEnabled: Boolean? = null,
    var languages: MutableList<String>? = null,
    var userCount: Int? = null,
    var statusCount: Int? = null,
    var domainCount: Int? = null,
)
