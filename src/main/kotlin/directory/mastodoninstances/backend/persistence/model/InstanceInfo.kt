package directory.mastodoninstances.backend.persistence.model

data class InstanceInfo(
    var title: String? = null,
    var shortDescription: String? = null,
    var description: String? = null,
    var version: String? = null,
    var thumbnail: String? = null,
    var languages: MutableList<String>? = mutableListOf(),
    var registrations: Boolean? = null,
    var approvalRequired: Boolean? = null,
    var invitesEnabled: Boolean? = null,
    var userCount: Int? = null,
    var statusCount: Int? = null,
    var domainCount: Int? = null,
    var housekeeping: InstanceHousekeeping? = null,
)
