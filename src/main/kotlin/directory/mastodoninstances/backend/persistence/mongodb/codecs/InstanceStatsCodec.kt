package directory.mastodoninstances.backend.persistence.mongodb.codecs

import directory.mastodoninstances.backend.persistence.model.InstanceStats
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext

private const val ID_FIELD_NAME = "_id"
private const val INSTANCE_ID_FIELD_NAME = "instanceId"
private const val TIMESTAMP_FIELD_NAME = "timestamp"
private const val USER_COUNT_FIELD_NAME = "userCount"
private const val STATUS_COUNT_FIELD_NAME = "statusCount"
private const val DOMAIN_COUNT_FIELD_NAME = "domainCount"

class InstanceStatsCodec : Codec<InstanceStats> {

    override fun encode(writer: BsonWriter, value: InstanceStats, encoderContext: EncoderContext?) {
        writer.writeStartDocument()
        writer.writeName(ID_FIELD_NAME)
        writeObjectIdOrNull(writer, value.id)
        writer.writeName(INSTANCE_ID_FIELD_NAME)
        writeObjectIdOrNull(writer, value.instanceId)
        writer.writeName(TIMESTAMP_FIELD_NAME)
        writeInstantOrNull(writer, value.timestamp)
        writer.writeName(USER_COUNT_FIELD_NAME)
        writeIntOrNull(writer, value.userCount)
        writer.writeName(STATUS_COUNT_FIELD_NAME)
        writeIntOrNull(writer, value.statusCount)
        writer.writeName(DOMAIN_COUNT_FIELD_NAME)
        writeIntOrNull(writer, value.domainCount)
        writer.writeEndDocument()
    }

    override fun getEncoderClass(): Class<InstanceStats> =
        InstanceStats::class.java

    override fun decode(reader: BsonReader, decoderContext: DecoderContext): InstanceStats {
        val instanceStats = InstanceStats()
        reader.readStartDocument()
        while (true) {
            val type = reader.readBsonType()
            if (type.equals(BsonType.END_OF_DOCUMENT)) { break }
            val fieldName = reader.readName()
            when (fieldName) {
                ID_FIELD_NAME -> instanceStats.id = readObjectIdOrNull(reader)
                INSTANCE_ID_FIELD_NAME -> instanceStats.instanceId = readObjectIdOrNull(reader)
                TIMESTAMP_FIELD_NAME -> instanceStats.timestamp = readInstantOrNull(reader)
                USER_COUNT_FIELD_NAME -> instanceStats.userCount = readIntOrNull(reader)
                STATUS_COUNT_FIELD_NAME -> instanceStats.statusCount = readIntOrNull(reader)
                DOMAIN_COUNT_FIELD_NAME -> instanceStats.domainCount = readIntOrNull(reader)
            }
        }
        reader.readEndDocument()
        return instanceStats
    }

}
