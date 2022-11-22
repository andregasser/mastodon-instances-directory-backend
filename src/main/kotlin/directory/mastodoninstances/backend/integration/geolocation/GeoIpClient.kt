package directory.mastodoninstances.backend.integration.geolocation

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import java.lang.RuntimeException

private const val RATE_LIMIT_REACHED_RESPONSE_BODY = "{\"success\":false,\"message\":\"You've hit the monthly limit\"}"

class GeoIpClient(private val webClient: WebClient, private val mapper: ObjectMapper) {

    fun getGeoIpInfoByIp(ip: String): Mono<GeoIpRecord> {
        return webClient.get()
            .uri("/$ip")
            .retrieve()
            .bodyToMono<String>()
            .flatMap { bodyAsString ->
                if (bodyAsString.equals(RATE_LIMIT_REACHED_RESPONSE_BODY)) {
                    Mono.error(RuntimeException("Monthly rate limit reached"))
                } else {
                    val geoIpRecord = mapper.readValue(bodyAsString, GeoIpRecord::class.java)
                    Mono.just(geoIpRecord)
                }
            }
    }

}
