package org.sw.spring.kotlin.exception

import jakarta.websocket.CloseReason
import org.springframework.http.HttpStatus

open class NotFoundException private constructor(
    title: String,
    detail: String,
    instance: String,
    cause: Throwable? = null
) : CommonException(
    HttpStatus.NOT_FOUND, title, detail, instance, cause
) {

    companion object {
        const val NOT_FOUND_PATH = "Not Found Path"

        fun ofPath(detail: String, cause: Throwable? = null): NotFoundException {
            return NotFoundException(NOT_FOUND_PATH, detail, "/NotFoundPage", cause)
        }
    }
}