package directory.mastodoninstances.backend.persistence.mongodb.codecs

import directory.mastodoninstances.backend.persistence.model.InstanceHousekeeping
import directory.mastodoninstances.backend.persistence.model.InstanceInfo
import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import org.bson.codecs.configuration.CodecRegistry
import java.lang.IllegalArgumentException

class InstanceInfoCodec(codecRegistry: CodecRegistry) : Codec<InstanceInfo> {

    private val URI_FIELD_NAME = "uri"
    private val TITLE_FIELD_NAME = "title"
    private val SHORT_DESCRIPTION_FIELD_NAME = "shortDescription"
    private val DESCRIPTION_FIELD_NAME = "description"
    private val VERSION_FIELD_NAME = "version"
    private val THUMBNAIL_FIELD_NAME = "thumbnail"
    private val LANGUAGES_FIELD_NAME = "languages"
    private val REGISTRATIONS_FIELD_NAME = "registrations"
    private val APPROVAL_REQUIRED_FIELD_NAME = "approvalRequired"
    private val INVITES_ENABLED_FIELD_NAME = "invitesEnabled"
    private val USER_COUNT_FIELD_NAME = "userCount"
    private val STATUS_COUNT_FIELD_NAME = "statusCount"
    private val DOMAIN_COUNT_FIELD_NAME = "domainCount"
    private val HOUSEKEEPING_FIELD_NAME = "housekeeping"

    private var instanceHousekeepingCodec: Codec<InstanceHousekeeping> = codecRegistry.get(InstanceHousekeeping::class.java)

    override fun encode(writer: BsonWriter, value: InstanceInfo?, encoderContext: EncoderContext?) {
        if (value == null) {
            writer.writeNull()
            return
        }
        writer.writeStartDocument()
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
        value.languages?.forEach {
            writeStringOrNull(writer, it)
        }
        writer.writeEndArray()
        writer.writeName(REGISTRATIONS_FIELD_NAME)
        writeBooleanOrNull(writer, value.registrations)
        writer.writeName(APPROVAL_REQUIRED_FIELD_NAME)
        writeBooleanOrNull(writer, value.approvalRequired)
        writer.writeName(INVITES_ENABLED_FIELD_NAME)
        writeBooleanOrNull(writer, value.invitesEnabled)
        writer.writeName(USER_COUNT_FIELD_NAME)
        writeIntOrNull(writer, value.userCount)
        writer.writeName(STATUS_COUNT_FIELD_NAME)
        writeIntOrNull(writer, value.statusCount)
        writer.writeName(DOMAIN_COUNT_FIELD_NAME)
        writeIntOrNull(writer, value.domainCount)
        writer.writeName(HOUSEKEEPING_FIELD_NAME)
        instanceHousekeepingCodec.encode(writer, value.housekeeping, encoderContext)
        writer.writeEndDocument()
    }

    override fun getEncoderClass(): Class<InstanceInfo> =
        InstanceInfo::class.java

    override fun decode(reader: BsonReader, decoderContext: DecoderContext): InstanceInfo? {
        when (reader.currentBsonType) {
            BsonType.NULL -> {
                reader.readNull()
                return null
            }

            BsonType.DOCUMENT -> {
                val instanceInfo = InstanceInfo()
                reader.readStartDocument()
                while (true) {
                    val type = reader.readBsonType()
                    if (type.equals(BsonType.END_OF_DOCUMENT)) {
                        break
                    }
                    val fieldName = reader.readName()
                    when (fieldName) {
                        TITLE_FIELD_NAME -> instanceInfo.title = readStringOrNull(reader)
                        SHORT_DESCRIPTION_FIELD_NAME -> instanceInfo.shortDescription = readStringOrNull(reader)
                        DESCRIPTION_FIELD_NAME -> instanceInfo.description = readStringOrNull(reader)
                        VERSION_FIELD_NAME -> instanceInfo.version = readStringOrNull(reader)
                        THUMBNAIL_FIELD_NAME -> instanceInfo.thumbnail = readStringOrNull(reader)
                        LANGUAGES_FIELD_NAME -> {
                            instanceInfo.languages = mutableListOf()
                            reader.readStartArray()
                            while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                                instanceInfo.languages?.add(reader.readString())
                            }
                            reader.readEndArray()
                        }
                        REGISTRATIONS_FIELD_NAME -> instanceInfo.registrations = readBooleanOrNull(reader)
                        APPROVAL_REQUIRED_FIELD_NAME -> instanceInfo.approvalRequired = readBooleanOrNull(reader)
                        INVITES_ENABLED_FIELD_NAME -> instanceInfo.invitesEnabled = readBooleanOrNull(reader)
                        USER_COUNT_FIELD_NAME -> instanceInfo.userCount = readIntOrNull(reader)
                        STATUS_COUNT_FIELD_NAME -> instanceInfo.statusCount = readIntOrNull(reader)
                        DOMAIN_COUNT_FIELD_NAME -> instanceInfo.domainCount = readIntOrNull(reader)
                        HOUSEKEEPING_FIELD_NAME -> instanceInfo.housekeeping = instanceHousekeepingCodec.decode(reader, decoderContext)
                    }
                }
                reader.readEndDocument()
                return instanceInfo
            }

            else -> throw IllegalArgumentException("Unknown BSON type: ${reader.readBsonType()}")
        }
    }

}
