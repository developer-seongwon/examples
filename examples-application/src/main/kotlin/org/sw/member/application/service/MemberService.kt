package org.sw.member.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.sw.member.application.dto.CreateMemberCommand
import org.sw.member.application.dto.MemberResponse
import org.sw.member.application.dto.UpdateMemberCommand
import org.sw.member.application.port.input.MemberUseCase
import org.sw.member.domain.exception.Conflict
import org.sw.member.domain.exception.ErrorCode
import org.sw.member.domain.exception.NotFound
import org.sw.member.domain.model.Member
import org.sw.member.domain.repository.MemberRepository

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository
) : MemberUseCase {

    @Transactional
    override fun createMember(command: CreateMemberCommand): MemberResponse {
        if (memberRepository.existsByEmail(command.email)) {
            throw Conflict(ErrorCode.DUPLICATE_EMAIL, "이미 존재하는 이메일입니다. email=${command.email}")
        }

        val member = Member(
            email = command.email,
            name = command.name,
            age = command.age
        )

        return MemberResponse.from(memberRepository.save(member))
    }

    override fun getMember(id: Long): MemberResponse {
        val member = memberRepository.findById(id)
            ?: throw NotFound(ErrorCode.MEMBER_NOT_FOUND, "회원을 찾을 수 없습니다. id=$id")
        return MemberResponse.from(member)
    }

    override fun getAllMembers(): List<MemberResponse> {
        return memberRepository.findAll().map { MemberResponse.from(it) }
    }

    @Transactional
    override fun updateMember(id: Long, command: UpdateMemberCommand): MemberResponse {
        val member = memberRepository.findById(id)
            ?: throw NotFound(ErrorCode.MEMBER_NOT_FOUND, "회원을 찾을 수 없습니다. id=$id")

        val updatedMember = member.update(name = command.name, age = command.age)
        return MemberResponse.from(memberRepository.save(updatedMember))
    }

    @Transactional
    override fun deleteMember(id: Long) {
        memberRepository.findById(id)
            ?: throw NotFound(ErrorCode.MEMBER_NOT_FOUND, "회원을 찾을 수 없습니다. id=$id")
        memberRepository.deleteById(id)
    }
}
