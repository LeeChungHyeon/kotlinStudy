package com.example.kotlinstudy.domain.member

data class MemberSaveReq(
    val email: String,
    val password: String,
    val role: Role
)

fun MemberSaveReq.toEntity(): Member {
    return Member(email = this.email, password = this.password, role = this.role)
}

