package org.sw.spring.kotlin.exception

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

open class CommonException(
    val status: HttpStatus,
    val title: String,
    val instance: String,
    val detail: String,
    cause: Throwable? = null,
) : RuntimeException(
    "$title, $detail", cause
) {
}