package org.sw.member.application.dto

import org.sw.member.domain.model.Member

data class CreateMemberCommand(
    val email: String,
    val name: String,
    val age: Int
)

data class UpdateMemberCommand(
    val name: String,
    val age: Int
)

data class MemberResponse(
    val id: Long,
    val email: String,
    val name: String,
    val age: Int
) {
    companion object {
        fun from(member: Member): MemberResponse {
            return MemberResponse(
                id = member.id!!,
                email = member.email,
                name = member.name,
                age = member.age
            )
        }
    }
}
