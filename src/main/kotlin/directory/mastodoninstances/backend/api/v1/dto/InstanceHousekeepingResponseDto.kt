package directory.mastodoninstances.backend.api.v1.dto

import java.time.Instant

data class InstanceHousekeepingResponseDto(
    var lastCheckResult: String? = null,
    var lastCheckFailureReason: String? = null,
    var lastCheck: Instant? = null,
    var nextCheck: Instant? = null,
)
