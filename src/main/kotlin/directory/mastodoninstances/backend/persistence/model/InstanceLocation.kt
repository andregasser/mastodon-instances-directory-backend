package directory.mastodoninstances.backend.persistence.model

data class InstanceLocation(
    var continent: String? = null,
    var country: String? = null,
    var countryCode: String? = null,
    var region: String? = null,
    var city: String? = null,
    var housekeeping: InstanceHousekeeping? = null,
)
