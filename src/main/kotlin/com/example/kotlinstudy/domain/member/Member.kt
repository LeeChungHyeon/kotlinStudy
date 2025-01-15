package com.example.kotlinstudy.domain.member

import com.example.kotlinstudy.domain.AuditingEntity
import jakarta.persistence.*

@Entity
@Table(name = "Member")
class Member(
    email: String,
    password: String,
    role: Role
) : AuditingEntity() {

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

}

enum class Role {
    USER, ADMIN
}