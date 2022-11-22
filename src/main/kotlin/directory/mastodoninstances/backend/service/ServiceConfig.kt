package directory.mastodoninstances.backend.service

import directory.mastodoninstances.backend.integration.database.InstanceRepository
import directory.mastodoninstances.backend.integration.database.InstanceStatsRepository
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServiceConfig {

    @Bean
    fun instanceService(instanceRepository: InstanceRepository) =
        InstanceService(instanceRepository)

    @Bean
    fun instanceStatsService(instanceStatsRepository: InstanceStatsRepository) =
        InstanceStatsService(instanceStatsRepository)

    @Bean
    fun instanceEventService(amqpTemplate: AmqpTemplate,
                     @Value("\${rabbitmq.exchange.name}") instanceEventExchangeName: String,
                     @Value("\${rabbitmq.checkqueue.routingkey}") checkQueueRoutingKey: String): NotificationService =
        NotificationService(amqpTemplate, instanceEventExchangeName, checkQueueRoutingKey)

}
