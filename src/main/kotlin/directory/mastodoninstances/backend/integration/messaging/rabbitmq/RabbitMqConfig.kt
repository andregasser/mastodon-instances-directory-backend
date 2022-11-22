package directory.mastodoninstances.backend.integration.messaging.rabbitmq

import directory.mastodoninstances.backend.integration.database.InstanceRepository
import directory.mastodoninstances.backend.jobs.CheckQueueProcessor
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMqConfig(
    @Value("\${rabbitmq.exchange.name}") val instanceEventExchangeName: String,
    @Value("\${rabbitmq.checkqueue.name}") val checkQueueName: String,
    @Value("\${rabbitmq.checkqueue.routingkey}") val checkQueueRoutingKey: String,
    @Value("\${rabbitmq.updatequeue.name}") val updateQueueName: String,
    @Value("\${rabbitmq.updatequeue.routingkey}") val updateQueueRoutingKey: String,
) {

    // This is now configured in the @RabbitListener annotation in CheckQueueProcessor

//    @Bean
//    fun instancesEventExchange(): DirectExchange =
//        DirectExchange(instanceEventExchangeName)
//
//    @Bean
//    fun checkQueue(): Queue =
//        Queue(checkQueueName, false)
//
//    @Bean
//    fun updateQueue(): Queue =
//        Queue(updateQueueName, false)

//    @Bean
//    fun checkBinding(@Qualifier("checkQueue") queue: Queue, exchange: DirectExchange): Binding =
//        BindingBuilder.bind(queue).to(exchange).with(checkQueueRoutingKey)
//
//    @Bean
//    fun updateBinding(@Qualifier("updateQueue") queue: Queue, exchange: DirectExchange): Binding =
//        BindingBuilder.bind(queue).to(exchange).with(updateQueueRoutingKey)


}
