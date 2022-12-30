package directory.mastodoninstances.backend.service

import directory.mastodoninstances.backend.clients.geolocation.GeoIpClient
import directory.mastodoninstances.backend.persistence.InstanceRepository
import directory.mastodoninstances.backend.persistence.InstanceStatsRepository
import directory.mastodoninstances.backend.service.check.DefaultCheckService
import directory.mastodoninstances.backend.service.geoiplookup.DefaultGeoIpLookupService
import directory.mastodoninstances.backend.service.geoiplookup.GeoIpLookupService
import directory.mastodoninstances.backend.service.instance.DefaultInstanceService
import directory.mastodoninstances.backend.service.instance.InstanceService
import directory.mastodoninstances.backend.service.mastodon.DefaultMastodonService
import directory.mastodoninstances.backend.service.mastodon.MastodonService
import directory.mastodoninstances.backend.service.notification.NotificationService
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServiceConfig {

    @Bean
    fun checkService(
        instanceService: InstanceService,
        mastodonService: MastodonService,
        geoIpLookupService: GeoIpLookupService,
    ) =
        DefaultCheckService(instanceService, mastodonService, geoIpLookupService)

    @Bean
    fun instanceService(instanceRepository: InstanceRepository, instanceStatsRepository: InstanceStatsRepository) =
        DefaultInstanceService(instanceRepository, instanceStatsRepository)

    @Bean
    fun instanceEventService(
        amqpTemplate: AmqpTemplate,
        @Value("\${rabbitmq.exchange.name}") instanceEventExchangeName: String,
        @Value("\${rabbitmq.checkqueue.routingkey}") checkQueueRoutingKey: String
    ): NotificationService =
        NotificationService(amqpTemplate, instanceEventExchangeName, checkQueueRoutingKey)

    @Bean
    fun mastodonService() = DefaultMastodonService()

    @Bean
    fun geoIpLookupService(geoIpClient: GeoIpClient) = DefaultGeoIpLookupService(geoIpClient)
}
