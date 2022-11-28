package directory.mastodoninstances.backend.api.v1.dto

data class InstanceLocationResponseDto(
    var continent: String? = null,
    var country: String? = null,
    var countryCode: String? = null,
    var region: String? = null,
    var city: String? = null,
    var housekeeping: InstanceHousekeepingResponseDto? = null,
)
