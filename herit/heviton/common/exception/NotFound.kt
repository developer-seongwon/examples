package net.herit.heviton.common.exception

import org.springframework.http.HttpStatus

open class NotFound private constructor(
    title: String,
    detail: String,
    instance: String,
    cause: Throwable? = null
): CommonException(HttpStatus.NOT_FOUND, title, instance, detail, cause){

    companion object{
        // TODO (title + instance) Enum
        const val PATH = "Not Found Path"

        fun ofPath(detail: String, cause: Throwable? = null): NotFound{
            return NotFound(title = PATH,
                detail = detail, instance = "/NotFoundPage", cause)
        }
    }
}