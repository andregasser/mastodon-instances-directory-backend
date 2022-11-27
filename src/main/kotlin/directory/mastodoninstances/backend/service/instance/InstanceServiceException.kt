package directory.mastodoninstances.backend.service.instance

class InstanceServiceException : RuntimeException {
    constructor(message: String, ex: Exception?): super(message, ex)
    constructor(message: String): super(message)
    constructor(ex: Exception): super(ex)
}
