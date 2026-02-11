package org.sw.member.infra.logback

import ch.qos.logback.classic.pattern.ClassicConverter
import ch.qos.logback.classic.spi.ILoggingEvent
import org.sw.member.domain.logging.SourceToTarget

class DirectionConverter : ClassicConverter() {
    override fun convert(event: ILoggingEvent): String {
        return event.mdcPropertyMap["direction"] ?: SourceToTarget.non().toString()
    }
}
