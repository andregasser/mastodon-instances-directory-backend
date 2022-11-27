package directory.mastodoninstances.backend.api.v1

import directory.mastodoninstances.backend.persistence.model.InstanceStats
import directory.mastodoninstances.backend.service.instance.InstanceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/instancestats")
class InstanceStatsController(private val instanceService: InstanceService) {

    @GetMapping("/{instanceId}")
    fun getInstanceStatsByInstanceId(@PathVariable("instanceId") instanceId: String): ResponseEntity<List<InstanceStats>> {
        val instanceStats = instanceService.getStatsByInstanceId(instanceId)
        return if (instanceStats.isNotEmpty()) {
            ResponseEntity<List<InstanceStats>>(instanceStats, HttpStatus.OK);
        } else {
            ResponseEntity<List<InstanceStats>>(null, HttpStatus.NOT_FOUND);
        }
    }
}
