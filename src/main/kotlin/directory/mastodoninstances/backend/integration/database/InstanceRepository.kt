package directory.mastodoninstances.backend.integration.database

import directory.mastodoninstances.backend.integration.database.model.Instance

interface InstanceRepository {

    fun create(instance: Instance)
    fun deleteByUri(uri: String)
    fun exists(uri: String): Boolean
    fun find(): List<Instance>
    fun findByUri(uri: String): Instance?
    fun findByInstanceId(instanceId: String): Instance?
    fun createIndices()

}
