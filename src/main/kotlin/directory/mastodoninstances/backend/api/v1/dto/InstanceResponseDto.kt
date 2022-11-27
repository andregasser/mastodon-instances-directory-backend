package directory.mastodoninstances.backend.api.v1.dto

import directory.mastodoninstances.backend.persistence.model.InstanceHousekeeping
import directory.mastodoninstances.backend.persistence.model.InstanceLocation
import directory.mastodoninstances.backend.persistence.model.InstanceSecurity

data class InstanceResponseDto(
    var id: String? = null,
    var info: InstanceInfoResponseDto? = null,
    var location: InstanceLocation? = null,
    var security: InstanceSecurity? = null,
)
