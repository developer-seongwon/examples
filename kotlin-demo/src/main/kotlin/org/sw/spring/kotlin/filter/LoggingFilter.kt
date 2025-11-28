package org.sw.spring.kotlin.filter

import kotlinx.coroutines.reactor.mono
import org.slf4j.LoggerFactory
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.ByteArrayOutputStream
import java.nio.channels.Channels
import java.nio.charset.StandardCharsets

@Component
class LoggingFilter : WebFilter {

    private val logger = LoggerFactory.getLogger(LoggingFilter::class.java)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val startTime = System.currentTimeMillis()
        val request = exchange.request
        
        // 요청 로깅
        logRequest(request)
        
        // 요청/응답 래핑
        val wrappedRequest = RequestLoggingDecorator(request)
        val wrappedResponse = ResponseLoggingDecorator(exchange.response, startTime)
        
        val mutatedExchange = exchange.mutate()
            .request(wrappedRequest)
            .response(wrappedResponse)
            .build()
        
        return chain.filter(mutatedExchange)
            .doOnSuccess {
                val duration = System.currentTimeMillis() - startTime
                logger.info("Request completed in ${duration}ms")
            }
            .doOnError { error ->
                val duration = System.currentTimeMillis() - startTime
                logger.error("Request failed after ${duration}ms", error)
            }
    }

    private fun logRequest(request: ServerHttpRequest) {
        logger.info("=== Incoming Request ===")
        logger.info("Method: ${request.method}")
        logger.info("URI: ${request.uri}")
        logger.info("Headers: ${request.headers}")
    }

    private inner class RequestLoggingDecorator(delegate: ServerHttpRequest) : 
        ServerHttpRequestDecorator(delegate) {
        
        override fun getBody(): Flux<DataBuffer> {
            return super.getBody().doOnNext { buffer ->
                val bodyContent = readBuffer(buffer)
                if (bodyContent.isNotEmpty()) {
                    logger.info("Request Body: $bodyContent")
                }
            }
        }
    }

    private inner class ResponseLoggingDecorator(
        delegate: ServerHttpResponse,
        private val startTime: Long
    ) : ServerHttpResponseDecorator(delegate) {
        
        override fun writeWith(body: org.reactivestreams.Publisher<out DataBuffer>): Mono<Void> {
            return super.writeWith(
                Flux.from(body).map { buffer ->
                    val responseBody = readBuffer(buffer)
                    val duration = System.currentTimeMillis() - startTime
                    
                    logger.info("=== Outgoing Response ===")
                    logger.info("Status: ${statusCode}")
                    logger.info("Headers: ${headers}")
                    if (responseBody.isNotEmpty()) {
                        logger.info("Response Body: $responseBody")
                    }
                    logger.info("Duration: ${duration}ms")
                    
                    // 버퍼를 다시 생성해서 반환 (중요!)
                    delegate.bufferFactory().wrap(responseBody.toByteArray(StandardCharsets.UTF_8))
                }
            )
        }
    }

    private fun readBuffer(buffer: DataBuffer): String {
        return try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            Channels.newChannel(byteArrayOutputStream).write(buffer.asByteBuffer().asReadOnlyBuffer())
            String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8)
        } catch (e: Exception) {
            logger.warn("Failed to read buffer", e)
            ""
        }
    }
}
