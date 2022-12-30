package directory.mastodoninstances.backend.jobs

import com.google.gson.Gson
import directory.mastodoninstances.backend.service.notification.NotificationService
import okhttp3.OkHttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import social.bigbone.MastodonClient
import social.bigbone.api.Handler
import social.bigbone.api.entity.Notification
import social.bigbone.api.entity.Status
import social.bigbone.api.exception.BigboneRequestException
import social.bigbone.api.method.Streaming
import java.net.URI
import javax.annotation.PostConstruct

class TimelineProcessor(
    private val notificationService: NotificationService,
    private val mastodonInstanceUri: String,
    private val mastodonInstanceAccessToken: String,
    ) {

    private val log: Logger = LoggerFactory.getLogger(TimelineProcessor::class.java)
    private val urisAddedToCheckQueue: MutableSet<String> = mutableSetOf()

    private fun processTimeline() {
        val client = MastodonClient.Builder(mastodonInstanceUri)
            .accessToken(mastodonInstanceAccessToken)
            .useStreamingApi()
            .build()

        val handler = object : Handler {
            override fun onStatus(status: Status) {
                val accountUrl = status.account?.url
                if (accountUrl != null) {
                    val uri = URI(accountUrl).host
                    if (!urisAddedToCheckQueue.contains(uri)) {
                        notificationService.addToCheckQueue(uri)
                        urisAddedToCheckQueue.add(uri)
                        log.debug("Added uri to checkQueue: $uri (first sight)")
                    } else {
                        log.debug("Uri skipped: $uri (already sent to checkQueue earlier)")
                    }
                } else {
                    log.debug("Status skipped, as status.account.url was null. Status: $status")
                }
            }
            override fun onNotification(notification: Notification) { }
            override fun onDelete(id: Long) { }
        }

        val streaming = Streaming(client)
        try {
            streaming.federatedPublic(handler)
        } catch(e: BigboneRequestException) {
            log.error("An error occurred while fetching the Mastodon federated timeline", e)
        }
    }

    @PostConstruct
    fun postConstruct() {
        processTimeline()
    }

}
