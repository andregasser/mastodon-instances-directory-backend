package directory.mastodoninstances.backend.service.geoiplookup

data class GeoIpLookupInfo(
    var ip: String? = null,
    var continent: String? = null,
    var country: String? = null,
    var countryCode: String? = null,
    var region: String? = null,
    var city: String? = null,
)
