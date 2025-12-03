package net.herit.heviton.common

import net.herit.heviton.common.exception.NotFound
import org.springframework.http.RequestEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GlobalController{

    @RequestMapping("/**")
    fun globalController(){
        throw NotFound.ofPath("Unsupported Request Path")
    }
}
