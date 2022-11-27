package directory.mastodoninstances.backend.jobs

import directory.mastodoninstances.backend.service.notification.NotificationService
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
}
