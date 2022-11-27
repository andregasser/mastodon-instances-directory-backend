package directory.mastodoninstances.backend.persistence.mongodb.codecs

import directory.mastodoninstances.backend.persistence.model.InstanceHousekeeping
import directory.mastodoninstances.backend.persistence.model.LastCheckResult
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry
import java.lang.IllegalArgumentException

private const val LAST_CHECK_RESULT_FIELD_NAME = "lastCheckResult"
private const val LAST_CHECK_FAILURE_REASON_FIELD_NAME = "lastCheckFailureReason"
private const val LAST_CHECK_FIELD_NAME = "lastCheck"
private const val NEXT_CHECK_FIELD_NAME = "nextCheck"

class InstanceHousekeepingCodec(codecRegistry: CodecRegistry) : Codec<InstanceHousekeeping> {

    private var lastCheckResultCodec: Codec<LastCheckResult> = codecRegistry.get(LastCheckResult::class.java)

    override fun encode(writer: BsonWriter, value: InstanceHousekeeping?, encoderContext: EncoderContext?) {
        if (value == null) {
            writer.writeNull()
            return
        }
        writer.writeStartDocument()
        writer.writeName(LAST_CHECK_RESULT_FIELD_NAME)
        lastCheckResultCodec.encode(writer, value.lastCheckResult, encoderContext)
        value.lastCheckFailureReason?.let {
            writer.writeName(LAST_CHECK_FAILURE_REASON_FIELD_NAME)
            writeStringOrNull(writer, it)
        }
        value.lastCheck?.let {
            writer.writeName(LAST_CHECK_FIELD_NAME)
            writeInstantOrNull(writer, it)
        }
        value.nextCheck?.let {
            writer.writeName(NEXT_CHECK_FIELD_NAME)
            writeInstantOrNull(writer, it)
        }
        writer.writeEndDocument()
    }

    override fun getEncoderClass(): Class<InstanceHousekeeping> =
        InstanceHousekeeping::class.java

    override fun decode(reader: BsonReader, decoderContext: DecoderContext): InstanceHousekeeping? {
        when (reader.currentBsonType) {
            BsonType.NULL -> {
                reader.readNull()
                return null
            }
            BsonType.DOCUMENT -> {
                val instanceHousekeeping = InstanceHousekeeping()
                reader.readStartDocument()
                while (true) {
                    val type = reader.readBsonType()
                    if (type.equals(BsonType.END_OF_DOCUMENT)) { break }
                    val fieldName = reader.readName()
                    when (fieldName) {
                        LAST_CHECK_RESULT_FIELD_NAME -> instanceHousekeeping.lastCheckResult = lastCheckResultCodec.decode(reader, decoderContext)
                        LAST_CHECK_FAILURE_REASON_FIELD_NAME -> instanceHousekeeping.lastCheckFailureReason = readStringOrNull(reader)
                        LAST_CHECK_FIELD_NAME -> instanceHousekeeping.lastCheck = readInstantOrNull(reader)
                        NEXT_CHECK_FIELD_NAME -> instanceHousekeeping.nextCheck = readInstantOrNull(reader)
                    }
                }
                reader.readEndDocument()
                return instanceHousekeeping
            }
            else -> throw IllegalArgumentException("Unknown BSON type: ${reader.readBsonType()}")
        }

    }

}
