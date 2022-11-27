package directory.mastodoninstances.backend.messaging.rabbitmq

import directory.mastodoninstances.backend.service.check.CheckService
import directory.mastodoninstances.backend.service.check.CheckServiceException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.ExchangeTypes
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener

class CheckQueueConsumer(
    private val checkService: CheckService,
) {
    private val log: Logger = LoggerFactory.getLogger(CheckQueueConsumer::class.java)

    @RabbitListener(
        bindings = [QueueBinding(
            value = Queue(value = "\${rabbitmq.checkqueue.name}", durable = "false", autoDelete = "false"),
            exchange = Exchange(value = "\${rabbitmq.exchange.name}", type = ExchangeTypes.DIRECT, ignoreDeclarationExceptions = "true"),
            key = arrayOf("\${rabbitmq.checkqueue.routingkey}")
        )]
    )
    fun processInstanceUri(uri: String) {
        log.debug("Processing new Mastodon uri $uri")
        try {
            if (!checkService.uriExists(uri)) {
                checkService.addUri(uri)
            }
        } catch (e: CheckServiceException) {
            log.error("")  // TODO: Improve error message
        }
    }
}
