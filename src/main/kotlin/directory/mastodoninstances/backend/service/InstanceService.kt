package directory.mastodoninstances.backend.service

import directory.mastodoninstances.backend.integration.database.InstanceRepository
import directory.mastodoninstances.backend.integration.database.model.Instance

class InstanceService(private val instanceRepository: InstanceRepository) {

    fun getInstances(): List<Instance> =
        instanceRepository.find()

    fun getInstanceByInstanceId(instanceId: String): Instance? =
        instanceRepository.findByInstanceId(instanceId)
}
