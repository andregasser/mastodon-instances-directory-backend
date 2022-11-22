package directory.mastodoninstances.backend.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.AmqpTemplate

class NotificationService(
    private val amqpTemplate: AmqpTemplate,
    private val instanceEventExchangeName: String,
    private val checkQueueRoutingKey: String
) {
    private val log: Logger = LoggerFactory.getLogger(NotificationService::class.java)
    private val urisNotifiedForChecking: MutableSet<String> = mutableSetOf()

    fun notifyCheckUri(uri: String) {
        if (!urisNotifiedForChecking.contains(uri)) {
            amqpTemplate.convertAndSend(instanceEventExchangeName, checkQueueRoutingKey, uri)
            log.debug("Added uri $uri to checkQueue")
        }

    }
}
