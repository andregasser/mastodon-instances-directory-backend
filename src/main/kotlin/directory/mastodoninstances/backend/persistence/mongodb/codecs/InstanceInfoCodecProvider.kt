package directory.mastodoninstances.backend.persistence.mongodb.codecs

import directory.mastodoninstances.backend.persistence.model.InstanceInfo
import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistry

class InstanceInfoCodecProvider : CodecProvider {

    override fun <T> get(clazz: Class<T>, registry: CodecRegistry): Codec<T>? {
        return if (clazz == InstanceInfo::class.java) {
            InstanceInfoCodec(registry) as Codec<T>
        } else null
    }

}
