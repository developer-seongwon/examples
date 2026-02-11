package org.sw.member.infra.repository.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.sw.member.infra.repository.jpa.entity.MemberJpaEntity

interface MemberJpaRepository : JpaRepository<MemberJpaEntity, Long> {
    fun existsByEmail(email: String): Boolean
}