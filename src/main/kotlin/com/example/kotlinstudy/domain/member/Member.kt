package com.example.kotlinstudy.domain.member

import com.example.kotlinstudy.domain.AuditingEntity
import jakarta.persistence.*

@Entity
@Table(name = "Member")
class Member(
    id: Long = 0,
    email: String,
    password: String,
    role: Role = Role.USER
) : AuditingEntity(id) {

    @Column(name = "email", nullable = false)
    var email: String = email
        protected set

    @Column(name = "password", nullable = false)
    var password: String = password
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: Role = role
        protected set

    fun toDto(): MemberRes {
        return MemberRes(
            id = this.id!!,
            email = this.email,
            password = this.password,
            role = this.role,
            createdAt = this.createAt,
            updatedAt = this.updateAt
        )
    }

    override fun toString(): String {
        return "Member(email='$email', password='$password', role=$role, createdAt=$createAt, updatedAt=$updateAt)"
    }

    companion object {
        fun createFakeMember(memberId: Long): Member {
            val member = Member(id = memberId, "admin@gmail.com", password = "1234")
            return member
        }
    }
}

enum class Role {
    USER, ADMIN
}