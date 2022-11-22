package directory.mastodoninstances.backend.integration.database.mongodb.codecs

import directory.mastodoninstances.backend.integration.database.model.Instance
import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistry


class InstanceCodecProvider : CodecProvider {

    override fun <T> get(clazz: Class<T>, registry: CodecRegistry): Codec<T>? {
        return if (clazz == Instance::class.java) {
            InstanceCodec(registry) as Codec<T>
        } else null
    }

}
