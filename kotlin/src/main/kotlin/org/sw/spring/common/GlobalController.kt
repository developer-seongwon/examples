package org.sw.spring.common

import org.springframework.http.RequestEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.sw.spring.common.exception.NotFoundException

@RestController
class GlobalController {

    @RequestMapping("/**")
    fun globalController(request: RequestEntity<*>) {
        throw NotFoundException.ofPath("Unsupported request: ${request.method} ${request.url.path}")
    }
}