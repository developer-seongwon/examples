package org.sw.spring.common.exception

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

open class CommonException(
    val status: HttpStatus,
    val title: String,
    val detail: String,
    val instance: String,
    cause: Throwable? = null,
) : RuntimeException(
    "$title, $detail($instance)", cause
) {
}