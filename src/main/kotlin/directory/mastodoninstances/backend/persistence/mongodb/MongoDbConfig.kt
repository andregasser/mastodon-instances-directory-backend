package directory.mastodoninstances.backend.persistence.mongodb

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import directory.mastodoninstances.backend.persistence.model.Instance
import directory.mastodoninstances.backend.persistence.model.InstanceStats
import directory.mastodoninstances.backend.persistence.mongodb.codecs.InstanceHousekeepingCodecProvider
import directory.mastodoninstances.backend.persistence.mongodb.codecs.InstanceCodecProvider
import directory.mastodoninstances.backend.persistence.mongodb.codecs.InstanceInfoCodecProvider
import directory.mastodoninstances.backend.persistence.mongodb.codecs.InstanceLocationCodecProvider
import directory.mastodoninstances.backend.persistence.mongodb.codecs.InstanceSecurityCodec
import directory.mastodoninstances.backend.persistence.mongodb.codecs.InstanceSecurityCodecProvider
import directory.mastodoninstances.backend.persistence.mongodb.codecs.InstanceStatsCodec
import directory.mastodoninstances.backend.persistence.mongodb.codecs.LastCheckResultCodec
import org.bson.codecs.configuration.CodecRegistries.fromCodecs
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class MongoDbConfig(@Value("\${db.connection.uri}") private val dbConnectionUri: String) {

    @Bean
    fun mongoClient(): MongoClient {
        val connectionString = ConnectionString(dbConnectionUri)
        val codecRegistry = fromRegistries(
                    fromCodecs(
                        LastCheckResultCodec(),
                        InstanceStatsCodec(),
                    ),
                    fromProviders(
                        InstanceCodecProvider(),
                        InstanceInfoCodecProvider(),
                        InstanceLocationCodecProvider(),
                        InstanceSecurityCodecProvider(),
                        InstanceHousekeepingCodecProvider(),
                    ),
                    MongoClientSettings.getDefaultCodecRegistry())

        val settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .codecRegistry(codecRegistry)
            .serverApi(
                ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build()
            )
            .build()
        return MongoClients.create(settings)
    }

    @Bean
    fun mongoDatabase(mongoClient: MongoClient) : MongoDatabase =
        mongoClient.getDatabase("InstanceDirectoryDb")

    @Bean
    fun instancesCollection(db: MongoDatabase) : MongoCollection<Instance> =
        db.getCollection("Instances", Instance::class.java)

    @Bean
    fun instanceStatsCollection(db: MongoDatabase) : MongoCollection<InstanceStats> =
        db.getCollection("InstanceStats", InstanceStats::class.java)

    @Bean
    fun instanceRepository(db: MongoDatabase, collection: MongoCollection<Instance>) =
        MongoDbInstanceRepository(collection).also { it.createIndices() }

    @Bean
    fun instanceStatsRepository(db: MongoDatabase, collection: MongoCollection<InstanceStats>) =
        MongoDbInstanceStatsRepository(collection).also { it.createIndices() }

}
