package net.herit.heviton.common.filter

import com.fasterxml.jackson.databind.ObjectMapper
import net.herit.logger.Marker
import net.herit.logger.Tracker
import net.herit.logger.TrackerFactory
import org.slf4j.LoggerFactory
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets

@Component
class LoggingFilter : WebFilter {
    companion object {
        val ATTRIBUTE_TRACKER = "LoggingFilter.Tracker";
    }

    private val logger = LoggerFactory.getLogger(LoggingFilter::class.java)
    private val mapper = ObjectMapper();

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        // 헤더에서 transactionId 가져오거나 새로 생성
        val tracker = exchange.request.headers.getFirst("x-transaction-id")
            ?.let { TrackerFactory.newInstance(it) }
            ?: TrackerFactory.newInstance()

        exchange.attributes.put(ATTRIBUTE_TRACKER, tracker);

        val prefix = "${exchange.request.uri.scheme.uppercase()} ${exchange.request.method} ${exchange.request.uri}"

        val request = LoggingRequestDecorator(exchange.request) { content ->
            loggingRequest(tracker, prefix, exchange.request.headers, content)
        }

        return chain.filter(exchange.mutate().request(request).build())
    }


    private fun loggingRequest(tracker: Tracker, prefix: String, headers: HttpHeaders, content: String?) {
        if (!logger.isInfoEnabled(Marker.Call)) {
            return
        }
        val header = mapper.writeValueAsString(headers.toSingleValueMap())
        if (MediaType.APPLICATION_JSON.isCompatibleWith(headers.contentType)) {
            logger.info(Marker.Call, "{} {} {} {}", tracker, prefix, header, content)
        } else {
            logger.info(Marker.Call, "{} {} {} {}", tracker, prefix, header, content)
        }
    }


    private inner class LoggingRequestDecorator(
        delegate: ServerHttpRequest,
        private val callback: (String?) -> Unit
    ) : ServerHttpRequestDecorator(delegate) {

        init {
            if (delegate.method == HttpMethod.GET) {
                callback(null)
            }
        }

        override fun getBody(): Flux<DataBuffer> {
            val contentType = delegate.headers.contentType
            val isLoggable = contentType?.isCompatibleWith(MediaType.APPLICATION_JSON) == true

            return super.getBody().doOnNext { buffer ->
                val body = if (isLoggable) {
                    StandardCharsets.UTF_8.decode(buffer.toByteBuffer().asReadOnlyBuffer()).toString()
                } else {
                    null
                }
                callback(body)
            }
        }
    }
}