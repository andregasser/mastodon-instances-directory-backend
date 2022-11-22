package directory.mastodoninstances.backend.integration.database

import directory.mastodoninstances.backend.integration.database.model.InstanceStats

interface InstanceStatsRepository {

    fun create(instanceStats: InstanceStats)
    fun findByInstanceId(instanceId: String): List<InstanceStats>
    fun createIndices()

}
