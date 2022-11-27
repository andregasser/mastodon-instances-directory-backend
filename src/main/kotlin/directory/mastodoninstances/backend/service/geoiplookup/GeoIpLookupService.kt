package directory.mastodoninstances.backend.service.geoiplookup

interface GeoIpLookupService {
    fun performLookup(uri: String): GeoIpLookupInfo
}
