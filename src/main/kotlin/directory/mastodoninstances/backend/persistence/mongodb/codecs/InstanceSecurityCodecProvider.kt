package directory.mastodoninstances.backend.persistence.mongodb.codecs

import directory.mastodoninstances.backend.persistence.model.InstanceSecurity
import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistry

class InstanceSecurityCodecProvider : CodecProvider {

    override fun <T> get(clazz: Class<T>, registry: CodecRegistry): Codec<T>? {
        return if (clazz == InstanceSecurity::class.java) {
            InstanceSecurityCodec(registry) as Codec<T>
        } else null
    }

}
