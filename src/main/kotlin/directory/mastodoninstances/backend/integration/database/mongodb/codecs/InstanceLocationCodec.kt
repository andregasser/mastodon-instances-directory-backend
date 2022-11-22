package directory.mastodoninstances.backend.integration.database.mongodb.codecs

import directory.mastodoninstances.backend.integration.database.model.InstanceLocation
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import java.lang.IllegalArgumentException

private const val CONTINENT_FIELD_NAME = "continent"
private const val COUNTRY_CODE_FIELD_NAME = "countryCode"
private const val COUNTRY_NAME_FIELD_NAME = "countryName"
private const val REGION_FIELD_NAME = "region"
private const val CITY_FIELD_NAME = "city"

class InstanceLocationCodec : Codec<InstanceLocation> {

    override fun encode(writer: BsonWriter, value: InstanceLocation?, encoderContext: EncoderContext?) {
        if (value == null) {
            writer.writeNull()
            return
        }
        writer.writeStartDocument()
        writer.writeName(CONTINENT_FIELD_NAME)
        writeStringOrNull(writer, value.continent)
        writer.writeName(COUNTRY_CODE_FIELD_NAME)
        writeStringOrNull(writer, value.countryCode)
        writer.writeName(COUNTRY_NAME_FIELD_NAME)
        writeStringOrNull(writer, value.countryName)
        writer.writeName(REGION_FIELD_NAME)
        writeStringOrNull(writer, value.region)
        writer.writeName(CITY_FIELD_NAME)
        writeStringOrNull(writer, value.city)
        writer.writeEndDocument()
    }

    override fun getEncoderClass(): Class<InstanceLocation> =
        InstanceLocation::class.java

    override fun decode(reader: BsonReader, decoderContext: DecoderContext): InstanceLocation? {
        when (reader.currentBsonType) {
            BsonType.NULL -> {
                reader.readNull()
                return null
            }
            BsonType.DOCUMENT -> {
                val instanceLocation = InstanceLocation()
                reader.readStartDocument()
                while (true) {
                    val type = reader.readBsonType()
                    if (type.equals(BsonType.END_OF_DOCUMENT)) { break }
                    val fieldName = reader.readName()
                    when (fieldName) {
                        CONTINENT_FIELD_NAME -> instanceLocation.continent = readStringOrNull(reader)
                        COUNTRY_CODE_FIELD_NAME -> instanceLocation.countryCode = readStringOrNull(reader)
                        COUNTRY_NAME_FIELD_NAME -> instanceLocation.countryName = readStringOrNull(reader)
                        REGION_FIELD_NAME -> instanceLocation.region = readStringOrNull(reader)
                        CITY_FIELD_NAME -> instanceLocation.city = readStringOrNull(reader)
                    }
                }
                reader.readEndDocument()
                return instanceLocation
            }
            else -> throw IllegalArgumentException("Unknown BSON type: ${reader.readBsonType()}")
        }
    }

}
