package directory.mastodoninstances.backend.persistence.mongodb.codecs

import directory.mastodoninstances.backend.persistence.model.Instance
import directory.mastodoninstances.backend.persistence.model.InstanceInfo
import directory.mastodoninstances.backend.persistence.model.InstanceLocation
import directory.mastodoninstances.backend.persistence.model.InstanceSecurity
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry

class InstanceCodec(codecRegistry: CodecRegistry) : Codec<Instance> {

    private val ID_FIELD_NAME = "_id"
    private val URI_FIELD_NAME = "uri"
    private val INFO_FIELD_NAME = "info"
    private val LOCATION_FIELD_NAME = "location"
    private val SECURITY_FIELD_NAME = "security"

    private var instanceInfoCodec: Codec<InstanceInfo> = codecRegistry.get(InstanceInfo::class.java)
    private var instanceLocationCodec: Codec<InstanceLocation> = codecRegistry.get(InstanceLocation::class.java)
    private var instanceSecurityCodec: Codec<InstanceSecurity> = codecRegistry.get(InstanceSecurity::class.java)

    override fun encode(writer: BsonWriter, value: Instance, encoderContext: EncoderContext?) {
        writer.writeStartDocument()
        writer.writeName(ID_FIELD_NAME)
        writeObjectIdOrNull(writer, value.id)
        writer.writeName(URI_FIELD_NAME)
        writeStringOrNull(writer, value.uri)
        writer.writeName(INFO_FIELD_NAME)
        instanceInfoCodec.encode(writer, value.info, encoderContext)
        writer.writeName(LOCATION_FIELD_NAME)
        instanceLocationCodec.encode(writer, value.location, encoderContext)
        writer.writeName(SECURITY_FIELD_NAME)
        instanceSecurityCodec.encode(writer, value.security, encoderContext)
        writer.writeEndDocument()
    }

    override fun getEncoderClass(): Class<Instance> =
        Instance::class.java

    override fun decode(reader: BsonReader, decoderContext: DecoderContext): Instance {
        val instance = Instance()
        reader.readStartDocument()
        while (true) {
            val type = reader.readBsonType()
            if (type.equals(BsonType.END_OF_DOCUMENT)) { break }
            val fieldName = reader.readName()
            when (fieldName) {
                ID_FIELD_NAME -> instance.id = readObjectIdOrNull(reader)
                URI_FIELD_NAME -> instance.uri = readStringOrNull(reader)
                INFO_FIELD_NAME -> instance.info = instanceInfoCodec.decode(reader, decoderContext)
                LOCATION_FIELD_NAME -> instance.location = instanceLocationCodec.decode(reader, decoderContext)
                SECURITY_FIELD_NAME -> instance.security = instanceSecurityCodec.decode(reader, decoderContext)
            }
        }
        reader.readEndDocument()
        return instance
    }

}
