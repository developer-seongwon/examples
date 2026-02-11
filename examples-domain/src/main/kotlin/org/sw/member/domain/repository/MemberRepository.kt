package org.sw.member.domain.repository

import org.sw.member.domain.model.Member

interface MemberRepository {
    fun save(member: Member): Member
    fun findById(id: Long): Member?
    fun findAll(): List<Member>
    fun deleteById(id: Long)
    fun existsByEmail(email: String): Boolean
}
