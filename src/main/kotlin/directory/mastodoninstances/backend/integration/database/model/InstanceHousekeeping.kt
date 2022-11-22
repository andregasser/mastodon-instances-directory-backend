package directory.mastodoninstances.backend.integration.database.model

import java.time.Instant

data class InstanceHousekeeping(
    var lastCheckResult: LastCheckResult? = null,
    var lastCheckFailureReason: String? = null,
    var lastCheck: Instant? = null,
    var nextCheck: Instant? = null,
)

enum class LastCheckResult {
    SUCCESS, FAILURE
}
