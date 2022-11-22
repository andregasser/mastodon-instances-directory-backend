package directory.mastodoninstances.backend.boundary.api.v1.dto

import directory.mastodoninstances.backend.integration.database.model.InstanceHousekeeping
import directory.mastodoninstances.backend.integration.database.model.InstanceLocation
import directory.mastodoninstances.backend.integration.database.model.InstanceSecurity

data class InstanceResponseDto(
    var id: String? = null,
    var uri: String? = null,
    var title: String? = null,
    var shortDescription: String? = null,
    var description: String? = null,
    var version: String? = null,
    var thumbnail: String? = null,
    var languages: MutableList<String> = mutableListOf(),
    var registrations: Boolean? = null,
    var approvalRequired: Boolean? = null,
    var invitesEnabled: Boolean? = null,
    var location: InstanceLocation? = null,
    var security: InstanceSecurity? = null,
    var housekeeping: InstanceHousekeeping? = null,
)
