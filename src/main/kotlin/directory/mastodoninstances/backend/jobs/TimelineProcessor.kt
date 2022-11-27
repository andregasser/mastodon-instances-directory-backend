package directory.mastodoninstances.backend.jobs

import com.google.gson.Gson
import com.sys1yagi.mastodon4j.MastodonClient
import com.sys1yagi.mastodon4j.api.Handler
import com.sys1yagi.mastodon4j.api.entity.Notification
import com.sys1yagi.mastodon4j.api.entity.Status
import com.sys1yagi.mastodon4j.api.exception.Mastodon4jRequestException
import com.sys1yagi.mastodon4j.api.method.Streaming
import directory.mastodoninstances.backend.service.notification.NotificationService
import okhttp3.OkHttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
        val client = MastodonClient.Builder(mastodonInstanceUri, OkHttpClient.Builder(), Gson())
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
        } catch(e: Mastodon4jRequestException) {
            log.error("An error occurred while fetching the Mastodon federated timeline", e)
        }
    }

    @PostConstruct
    fun postConstruct() {
        processTimeline()
    }

}
