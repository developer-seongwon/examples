package org.sw.member.http.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.sw.member.domain.logging.Tracker

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
class TransactionFilter(
    private val tracker: Tracker
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // HTTP 헤더로 제공받은 트랜잭션 아이디와 내부 트랜잭션 아이디는 별도로 사용됩니다.
        val transactionId = tracker.newTransactionId()
        try {
            MDC.put(Tracker.LOGGER_TRANSACTION_ID, transactionId)
            request.getHeader(Tracker.HEADER_TRANSACTION_ID)
                ?.also { response.setHeader(Tracker.HEADER_TRANSACTION_ID, it) }
                ?: run { response.setHeader(Tracker.HEADER_TRANSACTION_ID, transactionId) }

            filterChain.doFilter(request, response)
        } finally {
            MDC.clear()
        }
    }
}
