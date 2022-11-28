package directory.mastodoninstances.backend.clients.mastodonclient

import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class MastodonClient(private val webClient: WebClient) {

    private val API_V1_URL = "/api/v1"
    private val API_V2_URL = "/api/v2"

    fun getInstance(uri: String): Mono<InstanceV1> {
        return webClient.get()
            .uri("$uri$API_V1_URL/instance")
            .retrieve()
            .bodyToMono<InstanceV1>()
    }

    fun getInstanceV2(uri: String): Mono<InstanceV2> {
        return webClient.get()
            .uri("$uri$API_V2_URL/instance")
            .retrieve()
            .bodyToMono<InstanceV2>()
    }

}
