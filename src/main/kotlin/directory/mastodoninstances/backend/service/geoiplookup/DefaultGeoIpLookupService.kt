package directory.mastodoninstances.backend.service.geoiplookup

import directory.mastodoninstances.backend.clients.geolocation.GeoIpClient
import java.net.InetAddress
import java.time.Duration

class DefaultGeoIpLookupService(private val geoIpClient: GeoIpClient) : GeoIpLookupService {

    override fun performLookup(uri: String): GeoIpLookupInfo {
        try {
            val ip = getIpFromHostname(uri)
            val geoIpLookupResult = geoIpClient.getGeoIpInfoByIp(ip).block(Duration.ofSeconds(10))
            return GeoIpLookupInfo().apply {
                this.ip = geoIpLookupResult?.ip
                continent = geoIpLookupResult?.continent
                country = geoIpLookupResult?.country
                countryCode = geoIpLookupResult?.country_code
                region = geoIpLookupResult?.region
                city = geoIpLookupResult?.city
            }
        } catch (e: RuntimeException) {
            val errorMessage = "Unable to perform geo ip lookup for uri $uri"
            throw GeoIpLookupException(errorMessage, e)
        }
    }

    private fun getIpFromHostname(hostname: String): String {
        val host = InetAddress.getByName(hostname)
        return host.hostAddress
    }
}
