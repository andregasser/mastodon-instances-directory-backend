package directory.mastodoninstances.backend.integration.database.mongodb.codecs

import directory.mastodoninstances.backend.integration.database.model.InstanceHousekeeping
import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistry


class InstanceHousekeepingCodecProvider : CodecProvider {

    override fun <T> get(clazz: Class<T>, registry: CodecRegistry): Codec<T>? {
        return if (clazz == InstanceHousekeeping::class.java) {
            InstanceHousekeepingCodec(registry) as Codec<T>
        } else null
    }

}
