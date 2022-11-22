package directory.mastodoninstances.backend.integration.database.model

data class InstanceLocation(
    var continent: String? = null,
    var countryCode: String? = null,
    var countryName: String? = null,
    var region: String? = null,
    var city: String? = null,
)
