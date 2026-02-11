package org.sw.member.infra.repository

import org.springframework.stereotype.Repository
import org.sw.member.domain.model.Member
import org.sw.member.domain.repository.MemberRepository
import org.sw.member.infra.repository.jpa.MemberJpaRepository
import org.sw.member.infra.repository.jpa.entity.MemberJpaEntity

@Repository
class MemberPersistenceAdapter(
    private val memberJpaRepository: MemberJpaRepository
) : MemberRepository {

    override fun save(member: Member): Member {
        val entity = MemberJpaEntity.from(member)
        return memberJpaRepository.save(entity).toDomain()
    }

    override fun findById(id: Long): Member? {
        return memberJpaRepository.findById(id)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findAll(): List<Member> {
        return memberJpaRepository.findAll().map { it.toDomain() }
    }

    override fun deleteById(id: Long) {
        memberJpaRepository.deleteById(id)
    }

    override fun existsByEmail(email: String): Boolean {
        return memberJpaRepository.existsByEmail(email)
    }
}
