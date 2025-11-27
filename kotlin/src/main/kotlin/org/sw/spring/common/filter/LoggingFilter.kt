package org.sw.spring.common.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.io.UnsupportedEncodingException

@Component
class LoggingFilter : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
//        val wrappedRequest = ContentCachingRequestWrapper(request)
//        val wrappedResponse = ContentCachingResponseWrapper(response)
//
//        val startTime = System.currentTimeMillis()
//
//        try {
//            filterChain.doFilter(wrappedRequest, wrappedResponse)
//        } finally {
//            val duration = System.currentTimeMillis() - startTime
//            logRequest(wrappedRequest, duration)
//            logResponse(wrappedResponse, duration)
//            wrappedResponse.copyBodyToResponse()
//        }
    }

    private fun logRequest(request: ContentCachingRequestWrapper, duration: Long) {
        val uri = request.requestURI
        val method = request.method
        val queryString = request.queryString?.let { "?$it" } ?: ""
        val contentType = request.contentType ?: ""
        
        val requestBody = getContentAsString(
            request.contentAsByteArray,
            request.characterEncoding
        )

        logger.info(
            """
            |
            |>>> REQUEST <<<
            |Method: $method
            |URI: $uri$queryString
            |Content-Type: $contentType
            |Body: $requestBody
            """.trimMargin()
        )
    }

    private fun logResponse(response: ContentCachingResponseWrapper, duration: Long) {
        val status = response.status
        val contentType = response.contentType ?: ""
        
        val responseBody = getContentAsString(
            response.contentAsByteArray,
            response.characterEncoding
        )

        logger.info(
            """
            |
            |<<< RESPONSE <<<
            |Status: $status
            |Content-Type: $contentType
            |Duration: ${duration}ms
            |Body: $responseBody
            """.trimMargin()
        )
    }

    private fun getContentAsString(content: ByteArray, encoding: String): String {
        if (content.isEmpty()) {
            return ""
        }

        return try {
            String(content, charset(encoding))
        } catch (e: UnsupportedEncodingException) {
            String(content)
        }
    }
}