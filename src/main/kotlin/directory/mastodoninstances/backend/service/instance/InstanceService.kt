package directory.mastodoninstances.backend.service.instance

import directory.mastodoninstances.backend.persistence.model.Instance
import directory.mastodoninstances.backend.persistence.model.InstanceStats

interface InstanceService {
    fun createInstance(instance: Instance)
    fun getInstances(): List<Instance>
    fun getInstanceByInstanceId(instanceId: String): Instance?
    fun getStatsByInstanceId(instanceId: String): List<InstanceStats>
    fun uriExists(uri: String) : Boolean
}

