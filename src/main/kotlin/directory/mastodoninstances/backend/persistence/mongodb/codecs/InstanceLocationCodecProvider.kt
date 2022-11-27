package directory.mastodoninstances.backend.persistence.mongodb.codecs

import directory.mastodoninstances.backend.persistence.model.InstanceLocation
import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistry

class InstanceLocationCodecProvider : CodecProvider {

    override fun <T> get(clazz: Class<T>, registry: CodecRegistry): Codec<T>? {
        return if (clazz == InstanceLocation::class.java) {
            InstanceLocationCodec(registry) as Codec<T>
        } else null
    }

}
