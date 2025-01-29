package com.example.kotlinstudy.domain.member

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(name = "Member")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    email: String,
    password: String,
    role: Role = Role.USER,
    createAt: LocalDateTime,
    updateAt: LocalDateTime
) {

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

    @CreatedDate
    @Column(name = "create_at", nullable = false, updatable = false)
    var createAt: LocalDateTime = LocalDateTime.now()
        protected set

    @LastModifiedDate
    @Column(name = "update_at")
    var updateAt: LocalDateTime = LocalDateTime.now()
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
            val member = Member(id = memberId, "admin@gmail.com", password = "1234", createAt = LocalDateTime.now(), updateAt = LocalDateTime.now())
            return member
        }
    }
}

enum class Role {
    USER, ADMIN
}