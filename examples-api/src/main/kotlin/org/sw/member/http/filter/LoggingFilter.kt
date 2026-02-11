package org.sw.member.http.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.sw.member.domain.logging.LogMarker
import org.sw.member.domain.logging.SourceToTarget
import org.sw.member.domain.logging.SourceToTarget.Companion.withDirection

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
class LoggingFilter : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val time = System.currentTimeMillis()
        try {
            filterChain.doFilter(request, response)
        } finally {
            withDirection(SourceToTarget.left("HTTP")) {
                log.info(
                    LogMarker.OUT,
                    "{} {} {} ... {} {}ms",
                    request.protocol,
                    request.method,
                    request.requestURI,
                    response.status
                        .let { "$it ${HttpStatus.resolve(it)?.reasonPhrase ?: "???"}" },
                    (System.currentTimeMillis() - time)
                )
            }
        }
    }
}
