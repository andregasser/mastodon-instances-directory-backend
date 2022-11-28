package directory.mastodoninstances.backend.api.v1.dto

data class InstanceResponseDto(
    var id: String? = null,
    var info: InstanceInfoResponseDto? = null,
    var location: InstanceLocationResponseDto? = null,
)
