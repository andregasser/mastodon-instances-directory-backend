package directory.mastodoninstances.backend.jobs

import directory.mastodoninstances.backend.integration.database.InstanceRepository
import directory.mastodoninstances.backend.integration.database.InstanceStatsRepository
import directory.mastodoninstances.backend.integration.geolocation.GeoIpClient
import directory.mastodoninstances.backend.service.NotificationService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JobConfig {

    @Bean
    fun timelineProcessor(
        instanceEventService: NotificationService,
        @Value("\${mastodon.instance.uri}") mastodonInstanceUri: String,
        @Value("\${mastodon.instance.access-token}") mastodonInstanceAccessToken: String,
    ) =
        TimelineProcessor(instanceEventService, mastodonInstanceUri, mastodonInstanceAccessToken)

    @Bean
    fun checkQueueProcessor(instanceRepository: InstanceRepository, instanceStatsRepository: InstanceStatsRepository, geoIpClient: GeoIpClient) =
        CheckQueueProcessor(instanceRepository, instanceStatsRepository, geoIpClient)
}
