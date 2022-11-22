package directory.mastodoninstances.backend.integration.database.mongodb.codecs

import directory.mastodoninstances.backend.integration.database.model.InstanceSecurity
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import java.lang.IllegalArgumentException

private const val GRADE_FIELD_NAME = "grade"

class InstanceSecurityCodec : Codec<InstanceSecurity> {

    override fun encode(writer: BsonWriter, value: InstanceSecurity?, encoderContext: EncoderContext?) {
        if (value == null) {
            writer.writeNull()
            return
        }
        writer.writeStartDocument()
        writer.writeName(GRADE_FIELD_NAME)
        writeStringOrNull(writer, value.grade)
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
                    }
                }
                reader.readEndDocument()
                return instanceSecurity
            }
            else -> throw IllegalArgumentException("Unknown BSON type: ${reader.readBsonType()}")
        }
    }

}
