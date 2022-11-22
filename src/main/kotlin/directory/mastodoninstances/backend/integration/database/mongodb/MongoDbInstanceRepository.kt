package directory.mastodoninstances.backend.integration.database.mongodb

import com.mongodb.DuplicateKeyException
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import directory.mastodoninstances.backend.integration.database.InstanceRepository
import directory.mastodoninstances.backend.integration.database.model.Instance
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MongoDbInstanceRepository(private val collection: MongoCollection<Instance>) : InstanceRepository {

    private val log: Logger = LoggerFactory.getLogger(MongoDbInstanceRepository::class.java)

    private val ID_FIELD_NAME = "_id"
    private val URI_FIELD_NAME = "uri"

    override fun create(instance: Instance) {
        collection.insertOne(instance)
    }

    override fun deleteByUri(uri: String) {
        collection.deleteOne(eq(URI_FIELD_NAME, uri))
    }

    override fun exists(uri: String): Boolean {
        return collection.find(eq(URI_FIELD_NAME, uri)).count() > 0
    }

    override fun find(): List<Instance> {
        return collection.find().toList()
    }

    override fun findByUri(uri: String): Instance? {
        return collection.find(eq(URI_FIELD_NAME, uri)).first()
    }

    override fun findByInstanceId(instanceId: String): Instance? {
        return collection.find(eq(ID_FIELD_NAME, ObjectId(instanceId))).first()
    }

    override fun createIndices() {
        try {
            val indexOptions = IndexOptions().unique(true)
            val resultCreateIndex = collection.createIndex(Indexes.ascending(URI_FIELD_NAME), indexOptions)
            log.info("Index created: $resultCreateIndex")
        } catch (e: DuplicateKeyException) {
            log.error("index creation failed: duplicate field values encountered:\n", e)
        }
    }

}
