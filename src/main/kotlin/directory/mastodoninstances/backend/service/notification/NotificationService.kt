package directory.mastodoninstances.backend.service.notification

import org.springframework.amqp.core.AmqpTemplate

class NotificationService(
    private val amqpTemplate: AmqpTemplate,
    private val instanceEventExchangeName: String,
    private val checkQueueRoutingKey: String
) {

    fun addToCheckQueue(uri: String) {
        amqpTemplate.convertAndSend(instanceEventExchangeName, checkQueueRoutingKey, uri)
    }

}
