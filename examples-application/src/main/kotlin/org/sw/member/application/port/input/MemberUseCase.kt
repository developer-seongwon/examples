package org.sw.member.application.port.input

import org.sw.member.application.dto.CreateMemberCommand
import org.sw.member.application.dto.MemberResponse
import org.sw.member.application.dto.UpdateMemberCommand

interface MemberUseCase {
    fun createMember(command: CreateMemberCommand): MemberResponse
    fun getMember(id: Long): MemberResponse
    fun getAllMembers(): List<MemberResponse>
    fun updateMember(id: Long, command: UpdateMemberCommand): MemberResponse
    fun deleteMember(id: Long)
}
