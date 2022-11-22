package directory.mastodoninstances.backend.service

import directory.mastodoninstances.backend.integration.database.InstanceStatsRepository
import directory.mastodoninstances.backend.integration.database.model.InstanceStats

class InstanceStatsService(private val instanceStatsRepository: InstanceStatsRepository) {

    fun getStatsByInstanceId(instanceId: String): List<InstanceStats> =
        instanceStatsRepository.findByInstanceId(instanceId)

}
