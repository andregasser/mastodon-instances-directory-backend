package directory.mastodoninstances.backend.clients.mastodonclient

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Configuration
class MastodonClientConfig {

    @Bean
    fun mastodonClient(): MastodonClient {
        val httpClient = HttpClient.create()
            .responseTimeout(Duration.ofSeconds(5))

        val webClient = WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            //.baseUrl("https://ipwho.is")
            .defaultHeaders { httpHeaders ->
                httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            }
            .build()
        return MastodonClient(webClient)
    }

}
