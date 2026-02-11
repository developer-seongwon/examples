package org.sw.member.http.Interceptor

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import org.sw.member.domain.logging.LogMarker
import org.sw.member.domain.logging.SourceToTarget
import org.sw.member.domain.logging.SourceToTarget.Companion.withDirection

@Component
class LoggingInterceptor : HandlerInterceptor {

    private val log = LoggerFactory.getLogger(javaClass)
    private val objectMapper = ObjectMapper()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val request = request as ContentCachingRequestWrapper

        val uri = buildString {
            append(request.requestURI)
            request.queryString?.let { append("?$it") }
        }

        val headers = objectMapper.writeValueAsString(
            request.headerNames.toList().associateWith { request.getHeader(it) }
        )

        val body = if (isJson(request.contentType)) {
            request.contentAsByteArray.toString(Charsets.UTF_8)
        } else ""

        withDirection(SourceToTarget.leftIn("HTTP")) {
            log.info(
                LogMarker.CALL, "{} {} {} {} {}",
                request.protocol,
                request.method,
                uri,
                headers,
                body
            )
        }
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        val response = response as ContentCachingResponseWrapper

        val headers = objectMapper.writeValueAsString(
            response.headerNames.associateWith { response.getHeader(it) }
        )

        val body = if (isJson(response.contentType)) {
            response.contentAsByteArray.toString(Charsets.UTF_8)
        } else ""

        withDirection(SourceToTarget.leftOut("HTTP")) {
            log.info(
                LogMarker.CALL, "{} {} {}",
                HttpStatus.valueOf(response.status),
                headers,
                body
            )
        }
    }

    private fun isJson(contentType: String?): Boolean =
        contentType?.contains(MediaType.APPLICATION_JSON_VALUE) == true
}
