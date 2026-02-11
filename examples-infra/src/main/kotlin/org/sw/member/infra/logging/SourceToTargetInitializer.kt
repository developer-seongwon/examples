package org.sw.member.infra.logging

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import org.sw.member.domain.config.AppProperties
import org.sw.member.domain.logging.SourceToTarget

@Component
class SourceToTargetInitializer(
    private val appProperties: AppProperties
) {
    @PostConstruct
    fun init() {
        SourceToTarget.init(appProperties.name)
    }
}
