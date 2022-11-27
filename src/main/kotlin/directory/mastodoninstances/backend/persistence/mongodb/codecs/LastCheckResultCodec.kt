package directory.mastodoninstances.backend.persistence.mongodb.codecs

import directory.mastodoninstances.backend.persistence.model.LastCheckResult
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext

class LastCheckResultCodec : Codec<LastCheckResult> {

    override fun encode(writer: BsonWriter, value: LastCheckResult?, encoderContext: EncoderContext?) {
        writeStringOrNull(writer, value.toString())
    }

    override fun getEncoderClass(): Class<LastCheckResult> =
        LastCheckResult::class.java

    override fun decode(reader: BsonReader, decoderContext: DecoderContext): LastCheckResult? {
        val value = readStringOrNull(reader)
        return if (value == null) {
            null
        } else {
            LastCheckResult.valueOf(value)
        }
    }

}
