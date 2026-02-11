package org.sw.member.domain.model

data class Member(
    val id: Long? = null,
    val email: String,
    val name: String,
    val age: Int
) {
    init {
        require(email.isNotBlank()) { "이메일은 비어있을 수 없습니다." }
        require(name.isNotBlank()) { "이름은 비어있을 수 없습니다." }
        require(age in 0..150) { "나이는 0~150 사이여야 합니다." }
    }

    fun update(name: String, age: Int): Member {
        return copy(name = name, age = age)
    }
}
