package directory.mastodoninstances.backend.util

import directory.mastodoninstances.backend.api.v1.dto.InstanceHousekeepingResponseDto
import directory.mastodoninstances.backend.api.v1.dto.InstanceInfoResponseDto
import directory.mastodoninstances.backend.api.v1.dto.InstanceLocationResponseDto
import directory.mastodoninstances.backend.api.v1.dto.InstanceResponseDto
import directory.mastodoninstances.backend.persistence.model.Instance
import directory.mastodoninstances.backend.persistence.model.InstanceHousekeeping
import directory.mastodoninstances.backend.persistence.model.InstanceInfo
import directory.mastodoninstances.backend.persistence.model.InstanceLocation

fun Instance.toInstanceResponseDto(): InstanceResponseDto {
    val instanceResponseDto = InstanceResponseDto()
    instanceResponseDto.id = this.id.toString()
    instanceResponseDto.info = this.info?.toInstanceInfoResponseDto()
    instanceResponseDto.location = this.location?.toInstanceLocationResponseDto()
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
    instanceInfoResponseDto.housekeeping = this.housekeeping?.toInstanceHousekeepingResponseDto()
    return instanceInfoResponseDto
}

fun InstanceLocation.toInstanceLocationResponseDto(): InstanceLocationResponseDto {
    val instanceLocationResponseDto = InstanceLocationResponseDto()
    instanceLocationResponseDto.continent = this.continent
    instanceLocationResponseDto.country = this.country
    instanceLocationResponseDto.countryCode = this.countryCode
    instanceLocationResponseDto.region = this.region
    instanceLocationResponseDto.city = this.city
    instanceLocationResponseDto.housekeeping = this.housekeeping?.toInstanceHousekeepingResponseDto()
    return instanceLocationResponseDto
}

fun InstanceHousekeeping.toInstanceHousekeepingResponseDto(): InstanceHousekeepingResponseDto {
    val instanceHousekeepingResponseDto = InstanceHousekeepingResponseDto()
    instanceHousekeepingResponseDto.lastCheck = this.lastCheck
    instanceHousekeepingResponseDto.lastCheckResult = this.lastCheckResult.toString()
    instanceHousekeepingResponseDto.lastCheckFailureReason = this.lastCheckFailureReason
    instanceHousekeepingResponseDto.nextCheck = this.nextCheck
    return instanceHousekeepingResponseDto
}
