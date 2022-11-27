package directory.mastodoninstances.backend.persistence.mongodb.codecs

import directory.mastodoninstances.backend.persistence.model.InstanceHousekeeping
import directory.mastodoninstances.backend.persistence.model.InstanceLocation
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry
import java.lang.IllegalArgumentException

class InstanceLocationCodec(codecRegistry: CodecRegistry) : Codec<InstanceLocation> {
    private val CONTINENT_FIELD_NAME = "continent"
    private val COUNTRY_CODE_FIELD_NAME = "countryCode"
    private val COUNTRY_FIELD_NAME = "country"
    private val REGION_FIELD_NAME = "region"
    private val CITY_FIELD_NAME = "city"
    private val HOUSEKEEPING_FIELD_NAME = "housekeeping"

    private var instanceHousekeepingCodec: Codec<InstanceHousekeeping> = codecRegistry.get(InstanceHousekeeping::class.java)

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
        writer.writeName(COUNTRY_FIELD_NAME)
        writeStringOrNull(writer, value.country)
        writer.writeName(REGION_FIELD_NAME)
        writeStringOrNull(writer, value.region)
        writer.writeName(CITY_FIELD_NAME)
        writeStringOrNull(writer, value.city)
        writer.writeName(HOUSEKEEPING_FIELD_NAME)
        instanceHousekeepingCodec.encode(writer, value.housekeeping, encoderContext)
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
                    if (type.equals(BsonType.END_OF_DOCUMENT)) {
                        break
                    }
                    val fieldName = reader.readName()
                    when (fieldName) {
                        CONTINENT_FIELD_NAME -> instanceLocation.continent = readStringOrNull(reader)
                        COUNTRY_CODE_FIELD_NAME -> instanceLocation.countryCode = readStringOrNull(reader)
                        COUNTRY_FIELD_NAME -> instanceLocation.country = readStringOrNull(reader)
                        REGION_FIELD_NAME -> instanceLocation.region = readStringOrNull(reader)
                        CITY_FIELD_NAME -> instanceLocation.city = readStringOrNull(reader)
                        HOUSEKEEPING_FIELD_NAME -> instanceLocation.housekeeping = instanceHousekeepingCodec.decode(reader, decoderContext)
                    }
                }
                reader.readEndDocument()
                return instanceLocation
            }

            else -> throw IllegalArgumentException("Unknown BSON type: ${reader.readBsonType()}")
        }
    }
}
