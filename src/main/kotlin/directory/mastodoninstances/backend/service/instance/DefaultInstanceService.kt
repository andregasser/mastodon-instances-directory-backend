package directory.mastodoninstances.backend.service.instance

import directory.mastodoninstances.backend.persistence.InstanceRepository
import directory.mastodoninstances.backend.persistence.InstanceStatsRepository
import directory.mastodoninstances.backend.persistence.model.Instance
import directory.mastodoninstances.backend.persistence.model.InstanceStats
import org.bson.types.ObjectId
import java.time.Instant

class DefaultInstanceService(
    private val instanceRepository: InstanceRepository,
    private val instanceStatsRepository: InstanceStatsRepository
) : InstanceService {
    override fun createInstance(instance: Instance) {
        try {
            instanceRepository.create(instance)
        } catch (e: RuntimeException) {
            val errorMessage = "Unable to persist data into Instances collection for uri $instance.uri"
            throw InstanceServiceException(errorMessage, e)
        }

        val instanceStats = InstanceStats().apply {
            id = ObjectId()
            instanceId = instance.id
            timestamp = Instant.now()
            userCount = instance.info?.userCount
            statusCount = instance.info?.statusCount
            domainCount = instance.info?.domainCount
        }

        try {
            instanceStatsRepository.create(instanceStats)
        } catch (e: RuntimeException) {
            val errorMessage = "Unable to persist data into InstanceStats collection for uri $instance.uri"
            throw InstanceServiceException(errorMessage, e)
        }
    }

    override fun getInstances(): List<Instance> =
        instanceRepository.find()

    override fun getInstanceByInstanceId(instanceId: String): Instance? =
        instanceRepository.findByInstanceId(instanceId)

    override fun getStatsByInstanceId(instanceId: String): List<InstanceStats> =
        instanceStatsRepository.findByInstanceId(instanceId)

    override fun uriExists(uri: String): Boolean =
        instanceRepository.exists(uri)
}
