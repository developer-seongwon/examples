package org.sw.member.infra.repository.jpa.entity

import org.sw.member.domain.model.Member
import jakarta.persistence.*

@Entity
@Table(name = "members")
class MemberJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var age: Int
) {
    fun toDomain(): Member {
        return Member(
            id = id,
            email = email,
            name = name,
            age = age
        )
    }

    companion object {
        fun from(member: Member): MemberJpaEntity {
            return MemberJpaEntity(
                id = member.id,
                email = member.email,
                name = member.name,
                age = member.age
            )
        }
    }
}
