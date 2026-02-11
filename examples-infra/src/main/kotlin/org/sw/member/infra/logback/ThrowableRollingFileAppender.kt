package org.sw.member.infra.logback

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.IThrowableProxy
import ch.qos.logback.core.rolling.RollingFileAppender
import org.slf4j.Marker
import java.time.Instant

class ThrowableRollingFileAppender : RollingFileAppender<ILoggingEvent>() {

    /** 0 = 스택트레이스 없음(메시지만), 1 = 첫 번째 예외만(short), 2 = full(cause 체인 전체) */
    var level: Int = 1

    override fun writeOut(event: ILoggingEvent?) {
        if (event == null) {
            super.writeOut(event)
            return
        }

        // 메시지 먼저 출력 (throwable 제거)
        super.writeOut(stripThrowable(event))

        // level에 따라 스택트레이스 출력
        val proxy = event.throwableProxy ?: return
        when (level) {
            0 -> return
            1 -> writeException(event, proxy, "")
            else -> {
                var current: IThrowableProxy? = proxy
                var prefix = ""
                while (current != null) {
                    writeException(event, current, prefix)
                    prefix = "Caused by: "
                    current = current.cause
                }
            }
        }
    }

    private fun writeException(event: ILoggingEvent, proxy: IThrowableProxy, prefix: String) {
        split(event, "$prefix${proxy.className}: ${proxy.message}")
        proxy.stackTraceElementProxyArray
            ?.forEach { ste -> split(event, "\t$ste") }
    }

    private fun stripThrowable(original: ILoggingEvent) = object : ILoggingEvent by original {
        override fun getThrowableProxy(): IThrowableProxy? = null
        @Suppress("DEPRECATION") override fun getMarker(): Marker? = original.marker
        override fun getInstant(): Instant? = original.instant
        override fun getArgumentArray(): Array<out Any>? = original.argumentArray
    }

    private fun split(original: ILoggingEvent, newMessage: String) = object : ILoggingEvent by original {
        override fun getMessage(): String = newMessage
        override fun getFormattedMessage(): String = newMessage
        override fun getThrowableProxy(): IThrowableProxy? = null
        @Suppress("DEPRECATION") override fun getMarker(): Marker? = original.marker
        override fun getInstant(): Instant? = original.instant
        override fun getArgumentArray(): Array<out Any>? = original.argumentArray
    }.also {
        super.writeOut(it)
    }
}
