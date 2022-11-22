package directory.mastodoninstances.backend.boundary.api.v1

import directory.mastodoninstances.backend.integration.database.model.InstanceStats
import directory.mastodoninstances.backend.service.InstanceStatsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/instancestats")
class InstanceStatsController(private val instanceStatsService: InstanceStatsService) {

    @GetMapping("/{instanceId}")
    fun getInstanceStatsByInstanceId(@PathVariable("instanceId") instanceId: String): ResponseEntity<List<InstanceStats>> {
        val instanceStats = instanceStatsService.getStatsByInstanceId(instanceId)
        return if (instanceStats != null) {
            ResponseEntity<List<InstanceStats>>(instanceStats, HttpStatus.OK);
        } else {
            ResponseEntity<List<InstanceStats>>(null, HttpStatus.NOT_FOUND);
        }
    }
}
