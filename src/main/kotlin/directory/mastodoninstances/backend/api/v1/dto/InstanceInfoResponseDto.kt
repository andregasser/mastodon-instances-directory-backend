package directory.mastodoninstances.backend.api.v1.dto

data class InstanceInfoResponseDto(
    var title: String? = null,
    var shortDescription: String? = null,
    var description: String? = null,
    var version: String? = null,
    var thumbnail: String? = null,
    var languages: MutableList<String>? = null,
    var registrations: Boolean? = null,
    var approvalRequired: Boolean? = null,
    var invitesEnabled: Boolean? = null,
)

