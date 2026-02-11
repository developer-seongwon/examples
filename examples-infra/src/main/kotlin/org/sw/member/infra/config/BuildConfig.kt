package org.sw.member.infra.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Configuration
import org.sw.member.domain.config.AppProperties

@Configuration
class BuildConfig(
    private val buildProperties: BuildProperties,
    @Value("\${spring.application.name}") override val name: String,
    @Value("\${spring.application.node}") override val node: String,
    @Value("\${spring.application.domain}") override val domain: String
) : AppProperties {

    /** 버전 (e.g. "0.0.1-SNAPSHOT") */
    override val version: String get() = buildProperties.version

    /**
     * 고유 식별자를 생성한다.
     *
     * 규칙: {NAME}{노드}{메이저}{마이너}
     * 예) name=EXP, node=1, version=0.0.1-SNAPSHOT → EXP100
     */
    override val id: String
        get() {
            val parts = version.split("-")[0].split(".")
            val major = parts.getOrElse(0) { "0" }
            val minor = parts.getOrElse(1) { "0" }
            return "${name}${node}${major}${minor}"
        }
}
