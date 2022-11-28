package directory.mastodoninstances.backend.clients.mastodonclient

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import com.sys1yagi.mastodon4j.api.entity.Account
import com.sys1yagi.mastodon4j.api.entity.InstanceStats
import com.sys1yagi.mastodon4j.api.entity.InstanceUrls

data class InstanceV1(
    @JsonProperty("uri")
    val uri: String = "",

    @JsonProperty("title")
    val title: String = "",

    @JsonProperty("short_description")
    val shortDescription: String = "",

    @JsonProperty("description")
    val description: String = "",

    @JsonProperty("email")
    val email: String = "",

    @JsonProperty("version")
    val version: String = "",

    @JsonProperty("urls")
    val urls: InstanceUrls? = null,

    @JsonProperty("stats")
    val stats: InstanceStats? = null,

    @JsonProperty("thumbnail")
    val thumbnail: String = "",

    @JsonProperty("languages")
    val languages: List<String> = emptyList(),

    @JsonProperty("registrations")
    val registrations: Boolean = false,

    @JsonProperty("approval_required")
    val approvalRequired: Boolean = false,

    @JsonProperty("invites_enabled")
    val invitesEnabled: Boolean = false,

    @JsonProperty("contact_account")
    val contact_account: Account? = null
)

