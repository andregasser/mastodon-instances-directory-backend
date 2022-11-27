package directory.mastodoninstances.backend.persistence

import directory.mastodoninstances.backend.persistence.model.InstanceStats

interface InstanceStatsRepository {

    fun create(instanceStats: InstanceStats)
    fun findByInstanceId(instanceId: String): List<InstanceStats>
    fun createIndices()

}
