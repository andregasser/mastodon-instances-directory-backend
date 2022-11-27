package directory.mastodoninstances.backend.persistence

import directory.mastodoninstances.backend.persistence.model.Instance

interface InstanceRepository {

    fun create(instance: Instance)
    fun deleteByUri(uri: String)
    fun exists(uri: String): Boolean
    fun find(): List<Instance>
    fun findByUri(uri: String): Instance?
    fun findByInstanceId(instanceId: String): Instance?
    fun createIndices()

}
