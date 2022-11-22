package directory.mastodoninstances.backend.integration.database.mongodb

import com.mongodb.DuplicateKeyException
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import directory.mastodoninstances.backend.integration.database.InstanceStatsRepository
import directory.mastodoninstances.backend.integration.database.model.InstanceStats
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MongoDbInstanceStatsRepository(private val collection: MongoCollection<InstanceStats>) : InstanceStatsRepository {

    private val log: Logger = LoggerFactory.getLogger(MongoDbInstanceStatsRepository::class.java)

    private val INSTANCE_ID_FIELD_NAME = "instanceId"
    private val TIMESTAMP_FIELD_NAME = "timestamp"

    override fun create(instanceStats: InstanceStats) {
        collection.insertOne(instanceStats)
    }

    override fun findByInstanceId(instanceId: String): List<InstanceStats> {
        return collection.find(eq(INSTANCE_ID_FIELD_NAME, instanceId)).toList()
    }

    override fun createIndices() {
        try {
            val indexOptions = IndexOptions().unique(true)
            val resultCreateIndex = collection.createIndex(Indexes.ascending(INSTANCE_ID_FIELD_NAME, TIMESTAMP_FIELD_NAME), indexOptions)
            log.info("Index created: $resultCreateIndex")
        } catch (e: DuplicateKeyException) {
            log.error("index creation failed: duplicate field values encountered:\n", e)
        }
    }

}
