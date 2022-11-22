package directory.mastodoninstances.backend.boundary.api.v1

import directory.mastodoninstances.backend.boundary.api.v1.dto.InstanceAddRequestDto
import directory.mastodoninstances.backend.boundary.api.v1.dto.InstanceResponseDto
import directory.mastodoninstances.backend.service.InstanceService
import directory.mastodoninstances.backend.service.NotificationService
import directory.mastodoninstances.backend.util.toInstanceResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/instances")
class InstanceController(
    private val instanceService: InstanceService,
    private val notificationService: NotificationService
    ) {

    @GetMapping
    fun getInstances(): ResponseEntity<List<InstanceResponseDto>> {
        val listOfInstanceResponseDtos = instanceService.getInstances().map { it.toInstanceResponseDto() }
        return ResponseEntity<List<InstanceResponseDto>>(listOfInstanceResponseDtos, HttpStatus.OK);
    }

    @GetMapping("/{instanceId}")
    fun getInstanceByInstanceId(@PathVariable("instanceId") instanceId: String): ResponseEntity<InstanceResponseDto> {
        val instance = instanceService.getInstanceByInstanceId(instanceId)
        return if (instance != null) {
            ResponseEntity<InstanceResponseDto>(instance.toInstanceResponseDto(), HttpStatus.OK);
        } else {
            ResponseEntity<InstanceResponseDto>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    fun addInstance(@RequestBody instanceAddRequestDto: InstanceAddRequestDto) {
        notificationService.notifyCheckUri(instanceAddRequestDto.uri)
    }
}
