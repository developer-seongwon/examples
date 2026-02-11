package org.sw.member.http.controller

import org.sw.member.http.dto.CreateMemberRequest
import org.sw.member.http.dto.UpdateMemberRequest
import org.sw.member.application.dto.MemberResponse
import org.sw.member.application.port.input.MemberUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/members")
class MemberController(
    private val memberUseCase: MemberUseCase
) {

    @PostMapping
    fun createMember(@RequestBody request: CreateMemberRequest): ResponseEntity<MemberResponse> {
        val response = memberUseCase.createMember(request.toCommand())
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/{id}")
    fun getMember(@PathVariable id: Long): ResponseEntity<MemberResponse> {
        return ResponseEntity.ok(memberUseCase.getMember(id))
    }

    @GetMapping
    fun getAllMembers(): ResponseEntity<List<MemberResponse>> {
        return ResponseEntity.ok(memberUseCase.getAllMembers())
    }

    @PutMapping("/{id}")
    fun updateMember(
        @PathVariable id: Long,
        @RequestBody request: UpdateMemberRequest
    ): ResponseEntity<MemberResponse> {
        return ResponseEntity.ok(memberUseCase.updateMember(id, request.toCommand()))
    }

    @DeleteMapping("/{id}")
    fun deleteMember(@PathVariable id: Long): ResponseEntity<Void> {
        memberUseCase.deleteMember(id)
        return ResponseEntity.noContent().build()
    }
}
