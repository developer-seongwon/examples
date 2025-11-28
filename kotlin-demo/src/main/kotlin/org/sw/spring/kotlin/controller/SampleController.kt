package org.sw.spring.kotlin.controller

import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@RestController
@RequestMapping("/api")
class SampleController(
    private val webClient: WebClient
) {
    
    private val logger = LoggerFactory.getLogger(SampleController::class.java)

    @GetMapping("/sample")
    suspend fun getSample(@RequestParam(required = false) name: String?): Map<String, Any> {
        logger.info("SampleController - getSample called with name: $name")
        
        // 코루틴 방식으로 비동기 호출
        val healthResponse = webClient.get()
            .uri("/health")
            .retrieve()
            .awaitBody<Map<String, Any>>()
        
        logger.info("Received health response: $healthResponse")
        
        val response = mapOf(
            "message" to "Hello from Kotlin WebFlux with Coroutines!",
            "name" to (name ?: "Guest"),
            "healthCheck" to healthResponse,
            "timestamp" to System.currentTimeMillis()
        )
        
        logger.info("Returning response: $response")
        return response
    }

    @PostMapping("/sample")
    suspend fun postSample(@RequestBody request: SampleRequest): SampleResponse {
        logger.info("SampleController - postSample called with request: $request")
        
        val response = SampleResponse(
            result = "success",
            receivedMessage = request.message,
            processedAt = System.currentTimeMillis()
        )
        
        logger.info("Returning response: $response")
        return response
    }

    data class SampleRequest(
        val message: String,
        val value: Int? = null
    )

    data class SampleResponse(
        val result: String,
        val receivedMessage: String,
        val processedAt: Long
    )
}
