package directory.mastodoninstances.backend.integration.database.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.bson.types.ObjectId

data class Instance(
    var id: ObjectId? = null,
    var uri: String? = null,
    var title: String? = null,
    var shortDescription: String? = null,
    var description: String? = null,
    var version: String? = null,
    var thumbnail: String? = null,
    var languages: MutableList<String> = mutableListOf(),
    var registrations: Boolean? = null,

    @JsonProperty("approval_required")
    var approvalRequired: Boolean? = null,

    @JsonProperty("invites_enabled")
    var invitesEnabled: Boolean? = null,

    var location: InstanceLocation? = null,
    var security: InstanceSecurity? = null,
    var housekeeping: InstanceHousekeeping? = null,
)
