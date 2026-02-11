package org.sw.member.http.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.sw.member.domain.config.AppProperties
import org.sw.member.domain.logging.LogMarker
import java.nio.charset.StandardCharsets
import java.time.Instant

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 15)
class ExceptionFilter(
    private val appProperties: AppProperties
) : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (cause: Exception) {
            log.error(LogMarker.ERROR, "Unhandled exception: {}", cause.message, cause)
            writeProblemDetail(request, response)
        }
    }

    private fun writeProblemDetail(
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        response.status = 500
        response.contentType = MediaType.APPLICATION_PROBLEM_JSON_VALUE
        response.characterEncoding = StandardCharsets.UTF_8.name()

        val code = "${appProperties.name}-500-00"
        val type = "${appProperties.domain}/$code"
        val timestamp = Instant.now().toString()
        val body = """
            {
              "type": "$type",
              "title": "Internal Server Error",
              "status": 500,
              "detail": "내부 서버 오류",
              "instance": "${request.requestURI}",
              "code": "$code",
              "timestamp": "$timestamp"
            }
        """.trimIndent().replace("\\s+".toRegex(), " ").trim()

        response.writer.write(body)
        response.writer.flush()
    }
}
