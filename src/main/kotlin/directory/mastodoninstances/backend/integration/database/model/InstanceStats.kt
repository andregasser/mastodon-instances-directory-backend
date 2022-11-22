package directory.mastodoninstances.backend.integration.database.model

import org.bson.types.ObjectId
import java.time.Instant

data class InstanceStats(
    var id: ObjectId? = null,
    var instanceId: ObjectId? = null,
    var timestamp: Instant? = null,
    var userCount: Int? = null,
    var statusCount: Int? = null,
    var domainCount: Int? = null,
)
