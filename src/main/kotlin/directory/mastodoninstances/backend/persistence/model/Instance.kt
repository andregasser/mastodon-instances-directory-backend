package directory.mastodoninstances.backend.persistence.model

import org.bson.types.ObjectId

data class Instance(
    var id: ObjectId? = null,
    var uri: String? = null,
    var info: InstanceInfo? = null,
    var location: InstanceLocation? = null,
)
