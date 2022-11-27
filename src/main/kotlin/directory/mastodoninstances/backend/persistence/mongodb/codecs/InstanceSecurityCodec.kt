package directory.mastodoninstances.backend.persistence.mongodb.codecs

import directory.mastodoninstances.backend.persistence.model.InstanceHousekeeping
import directory.mastodoninstances.backend.persistence.model.InstanceSecurity
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry
import java.lang.IllegalArgumentException



class InstanceSecurityCodec(codecRegistry: CodecRegistry) : Codec<InstanceSecurity> {

    private val GRADE_FIELD_NAME = "grade"
    private val HOUSEKEEPING_FIELD_NAME = "housekeeping"

    private var instanceHousekeepingCodec: Codec<InstanceHousekeeping> = codecRegistry.get(InstanceHousekeeping::class.java)

    override fun encode(writer: BsonWriter, value: InstanceSecurity?, encoderContext: EncoderContext?) {
        if (value == null) {
            writer.writeNull()
            return
        }
        writer.writeStartDocument()
        writer.writeName(GRADE_FIELD_NAME)
        writeStringOrNull(writer, value.grade)
        writer.writeName(HOUSEKEEPING_FIELD_NAME)
        instanceHousekeepingCodec.encode(writer, value.housekeeping, encoderContext)
        writer.writeEndDocument()
    }

    override fun getEncoderClass(): Class<InstanceSecurity> =
        InstanceSecurity::class.java

    override fun decode(reader: BsonReader, decoderContext: DecoderContext): InstanceSecurity? {
        when (reader.currentBsonType) {
            BsonType.NULL -> {
                reader.readNull()
                return null
            }
            BsonType.DOCUMENT -> {
                val instanceSecurity = InstanceSecurity()
                reader.readStartDocument()
                while (true) {
                    val type = reader.readBsonType()
                    if (type.equals(BsonType.END_OF_DOCUMENT)) { break }
                    val fieldName = reader.readName()
                    when (fieldName) {
                        GRADE_FIELD_NAME -> instanceSecurity.grade = readStringOrNull(reader)
                        HOUSEKEEPING_FIELD_NAME -> instanceSecurity.housekeeping = instanceHousekeepingCodec.decode(reader, decoderContext)
                    }
                }
                reader.readEndDocument()
                return instanceSecurity
            }
            else -> throw IllegalArgumentException("Unknown BSON type: ${reader.readBsonType()}")
        }
    }

}
