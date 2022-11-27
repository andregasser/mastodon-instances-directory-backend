package directory.mastodoninstances.backend.persistence.model

data class InstanceSecurity(
    var grade: String? = null,
    var housekeeping: InstanceHousekeeping? = null,
)
