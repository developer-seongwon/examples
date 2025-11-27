package org.sw.spring.common.exception

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
            return NotFoundException(
                title = NOT_FOUND_PATH,
                detail = detail,
                instance = "/NotFoundPage",
                cause = cause
            )
        }
    }
}