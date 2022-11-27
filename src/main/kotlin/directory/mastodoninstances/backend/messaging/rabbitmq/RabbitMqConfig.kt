package directory.mastodoninstances.backend.messaging.rabbitmq

import directory.mastodoninstances.backend.service.check.CheckService
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
    @Bean
    fun checkQueueConsumer(checkService: CheckService) =
        CheckQueueConsumer(checkService)


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
