package directory.mastodoninstances.backend.util

import directory.mastodoninstances.backend.api.v1.dto.InstanceInfoResponseDto
import directory.mastodoninstances.backend.api.v1.dto.InstanceResponseDto
import directory.mastodoninstances.backend.persistence.model.Instance
import directory.mastodoninstances.backend.persistence.model.InstanceInfo

fun Instance.toInstanceResponseDto(): InstanceResponseDto {
    val instanceResponseDto = InstanceResponseDto()
    instanceResponseDto.id = this.id.toString()
    instanceResponseDto.info = this.info?.toInstanceInfoResponseDto()
    instanceResponseDto.location = this.location // TODO: Convert to DTO
    instanceResponseDto.security = this.security // TODO: Convert to DTO
    return instanceResponseDto
}

fun InstanceInfo.toInstanceInfoResponseDto(): InstanceInfoResponseDto {
    val instanceInfoResponseDto = InstanceInfoResponseDto()
    instanceInfoResponseDto.title = this.title
    instanceInfoResponseDto.shortDescription = this.shortDescription
    instanceInfoResponseDto.description = this.description
    instanceInfoResponseDto.version = this.version
    instanceInfoResponseDto.thumbnail = this.thumbnail
    instanceInfoResponseDto.languages = this.languages
    instanceInfoResponseDto.registrations = this.registrations
    instanceInfoResponseDto.approvalRequired = this.approvalRequired
    instanceInfoResponseDto.invitesEnabled = this.invitesEnabled
    return instanceInfoResponseDto
}
