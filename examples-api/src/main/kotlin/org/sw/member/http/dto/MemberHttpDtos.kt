package org.sw.member.http.dto

import org.sw.member.application.dto.CreateMemberCommand
import org.sw.member.application.dto.UpdateMemberCommand

data class CreateMemberRequest(
    val email: String,
    val name: String,
    val age: Int
) {
    fun toCommand(): CreateMemberCommand {
        return CreateMemberCommand(
            email = email,
            name = name,
            age = age
        )
    }
}

data class UpdateMemberRequest(
    val name: String,
    val age: Int
) {
    fun toCommand(): UpdateMemberCommand {
        return UpdateMemberCommand(
            name = name,
            age = age
        )
    }
}
