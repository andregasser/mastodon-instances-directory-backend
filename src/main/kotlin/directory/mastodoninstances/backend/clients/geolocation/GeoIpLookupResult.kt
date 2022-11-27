package directory.mastodoninstances.backend.clients.geolocation

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeoIpLookupResult(
    var ip: String? = null,
    var continent: String? = null,
    var country: String? = null,
    var country_code: String? = null,
    var region: String? = null,
    var city: String? = null,
)
