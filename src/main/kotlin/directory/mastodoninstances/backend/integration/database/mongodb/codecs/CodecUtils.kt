package directory.mastodoninstances.backend.integration.database.mongodb.codecs

import org.bson.BsonReader
import org.bson.BsonType
import org.bson.BsonWriter
import org.bson.types.ObjectId
import java.time.Instant

fun readStringOrNull(reader: BsonReader): String? {
    return when (reader.currentBsonType) {
        BsonType.NULL -> {
            reader.readNull()
            return null
        }
        BsonType.STRING -> reader.readString()
        else -> throw IllegalArgumentException("Unknown BSON type: ${reader.readBsonType()}", )
    }
}

fun readObjectIdOrNull(reader: BsonReader): ObjectId? {
    return when (reader.currentBsonType) {
        BsonType.NULL -> {
            reader.readNull()
            return null
        }
        BsonType.OBJECT_ID -> reader.readObjectId()
        else -> throw IllegalArgumentException("Unknown BSON type: ${reader.readBsonType()}", )
    }
}

fun readBooleanOrNull(reader: BsonReader): Boolean? {
    return when (reader.currentBsonType) {
        BsonType.NULL -> {
            reader.readNull()
            return null
        }
        BsonType.BOOLEAN -> reader.readBoolean()
        else -> throw IllegalArgumentException("Unknown BSON type: ${reader.readBsonType()}", )
    }
}

fun readInstantOrNull(reader: BsonReader): Instant? {
    return when (reader.currentBsonType) {
        BsonType.NULL -> {
            reader.readNull()
            return null
        }
        BsonType.DATE_TIME -> Instant.ofEpochMilli(reader.readDateTime())
        else -> throw IllegalArgumentException("Unknown BSON type: ${reader.readBsonType()}", )
    }
}

fun readIntOrNull(reader: BsonReader): Int? {
    return when (reader.currentBsonType) {
        BsonType.NULL -> {
            reader.readNull()
            return null
        }
        BsonType.INT32 -> reader.readInt32()
        else -> throw IllegalArgumentException("Unknown BSON type: ${reader.readBsonType()}", )
    }
}

fun writeObjectIdOrNull(writer: BsonWriter, objectId: ObjectId?) {
    if  (objectId == null) {
        writer.writeNull()
    } else {
        writer.writeObjectId(objectId)
    }
}

fun writeInstantOrNull(writer: BsonWriter, instant: Instant?) {
    if (instant == null) {
        writer.writeNull()
    } else {
        writer.writeDateTime(instant.toEpochMilli())
    }
}

fun writeIntOrNull(writer: BsonWriter, int: Int?) {
    if (int == null) {
        writer.writeNull()
    } else {
        writer.writeInt32(int)
    }
}

fun writeStringOrNull(writer: BsonWriter, string: String?) {
    if (string == null) {
        writer.writeNull()
    } else {
        writer.writeString(string)
    }
}

fun writeBooleanOrNull(writer: BsonWriter, boolean: Boolean?) {
    if (boolean == null) {
        writer.writeNull()
    } else {
        writer.writeBoolean(boolean)
    }
}
