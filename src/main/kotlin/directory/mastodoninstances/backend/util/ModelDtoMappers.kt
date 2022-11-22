package directory.mastodoninstances.backend.util

import directory.mastodoninstances.backend.boundary.api.v1.dto.InstanceResponseDto
import directory.mastodoninstances.backend.integration.database.model.Instance

fun Instance.toInstanceResponseDto(): InstanceResponseDto {
    val instanceResponseDto = InstanceResponseDto()
    instanceResponseDto.id = this.id.toString()
    instanceResponseDto.uri = this.uri
    instanceResponseDto.title = this.title
    instanceResponseDto.shortDescription = this.shortDescription
    instanceResponseDto.description = this.description
    instanceResponseDto.version = this.version
    instanceResponseDto.thumbnail = this.thumbnail
    instanceResponseDto.languages = this.languages
    instanceResponseDto.registrations = this.registrations
    instanceResponseDto.approvalRequired = this.approvalRequired
    instanceResponseDto.invitesEnabled = this.invitesEnabled
    instanceResponseDto.location = this.location
    instanceResponseDto.security = this.security
    instanceResponseDto.housekeeping = this.housekeeping
    return instanceResponseDto
}
