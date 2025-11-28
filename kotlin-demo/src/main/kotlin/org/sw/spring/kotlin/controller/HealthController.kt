package org.sw.spring.kotlin.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class HealthController {

    private val logger = LoggerFactory.getLogger(HealthController::class.java)

    @GetMapping("/health")
    suspend fun health(): Map<String, Any> {
        logger.info("HealthController - health check called")
        
        val response = mapOf(
            "status" to "UP",
            "timestamp" to LocalDateTime.now().toString(),
            "service" to "kotlin-webflux-demo"
        )
        
        logger.info("Health check response: $response")
        return response
    }
}
