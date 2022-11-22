package directory.mastodoninstances.backend.integration.database.mongodb.codecs

import directory.mastodoninstances.backend.integration.database.model.Instance
import directory.mastodoninstances.backend.integration.database.model.InstanceHousekeeping
import directory.mastodoninstances.backend.integration.database.model.InstanceLocation
import directory.mastodoninstances.backend.integration.database.model.InstanceSecurity
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry

private const val ID_FIELD_NAME = "_id"
private const val URI_FIELD_NAME = "uri"
private const val TITLE_FIELD_NAME = "title"
private const val SHORT_DESCRIPTION_FIELD_NAME = "shortDescription"
private const val DESCRIPTION_FIELD_NAME = "description"
private const val VERSION_FIELD_NAME = "version"
private const val THUMBNAIL_FIELD_NAME = "thumbnail"
private const val LANGUAGES_FIELD_NAME = "languages"
private const val REGISTRATIONS_FIELD_NAME = "registrations"
private const val APPROVAL_REQUIRED_FIELD_NAME = "approvalRequired"
private const val INVITES_ENABLED_FIELD_NAME = "invitesEnabled"
private const val LOCATION_FIELD_NAME = "location"
private const val SECURITY_FIELD_NAME = "security"
private const val HOUSEKEEPING_FIELD_NAME = "housekeeping"

class InstanceCodec(codecRegistry: CodecRegistry) : Codec<Instance> {

    private var instanceLocationCodec: Codec<InstanceLocation> = codecRegistry.get(InstanceLocation::class.java)
    private var instanceSecurityCodec: Codec<InstanceSecurity> = codecRegistry.get(InstanceSecurity::class.java)
    private var instanceHousekeepingCodec: Codec<InstanceHousekeeping> = codecRegistry.get(InstanceHousekeeping::class.java)

    override fun encode(writer: BsonWriter, value: Instance, encoderContext: EncoderContext?) {
        writer.writeStartDocument()
        writer.writeName(ID_FIELD_NAME)
        writeObjectIdOrNull(writer, value.id)
        writer.writeName(URI_FIELD_NAME)
        writeStringOrNull(writer, value.uri)
        writer.writeName(TITLE_FIELD_NAME)
        writeStringOrNull(writer, value.title)
        writer.writeName(SHORT_DESCRIPTION_FIELD_NAME)
        writeStringOrNull(writer, value.shortDescription)
        writer.writeName(DESCRIPTION_FIELD_NAME)
        writeStringOrNull(writer, value.description)
        writer.writeName(VERSION_FIELD_NAME)
        writeStringOrNull(writer, value.version)
        writer.writeName(THUMBNAIL_FIELD_NAME)
        writeStringOrNull(writer, value.thumbnail)
        writer.writeStartArray(LANGUAGES_FIELD_NAME)
        value.languages.forEach {
            writeStringOrNull(writer, it)
        }
        writer.writeEndArray()
        writer.writeName(REGISTRATIONS_FIELD_NAME)
        writeBooleanOrNull(writer, value.registrations)
        writer.writeName(APPROVAL_REQUIRED_FIELD_NAME)
        writeBooleanOrNull(writer, value.approvalRequired)
        writer.writeName(INVITES_ENABLED_FIELD_NAME)
        writeBooleanOrNull(writer, value.invitesEnabled)
        writer.writeName(LOCATION_FIELD_NAME)
        instanceLocationCodec.encode(writer, value.location, encoderContext)
        writer.writeName(SECURITY_FIELD_NAME)
        instanceSecurityCodec.encode(writer, value.security, encoderContext)
        writer.writeName(HOUSEKEEPING_FIELD_NAME)
        instanceHousekeepingCodec.encode(writer, value.housekeeping, encoderContext)
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
                TITLE_FIELD_NAME -> instance.title = readStringOrNull(reader)
                SHORT_DESCRIPTION_FIELD_NAME -> instance.shortDescription = readStringOrNull(reader)
                DESCRIPTION_FIELD_NAME -> instance.description = readStringOrNull(reader)
                VERSION_FIELD_NAME -> instance.version = readStringOrNull(reader)
                THUMBNAIL_FIELD_NAME -> instance.thumbnail = readStringOrNull(reader)
                LANGUAGES_FIELD_NAME -> {
                    instance.languages = mutableListOf()
                    reader.readStartArray()
                    while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                        instance.languages.add(reader.readString())
                    }
                    reader.readEndArray()
                }
                REGISTRATIONS_FIELD_NAME -> instance.registrations = readBooleanOrNull(reader)
                APPROVAL_REQUIRED_FIELD_NAME -> instance.approvalRequired = readBooleanOrNull(reader)
                INVITES_ENABLED_FIELD_NAME -> instance.invitesEnabled = readBooleanOrNull(reader)
                LOCATION_FIELD_NAME -> instance.location = instanceLocationCodec.decode(reader, decoderContext)
                SECURITY_FIELD_NAME -> instance.security = instanceSecurityCodec.decode(reader, decoderContext)
                HOUSEKEEPING_FIELD_NAME -> instance.housekeeping = instanceHousekeepingCodec.decode(reader, decoderContext)
            }
        }
        reader.readEndDocument()
        return instance
    }

}
