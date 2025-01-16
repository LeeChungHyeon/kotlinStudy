package com.example.kotlinstudy.domain.member

import jakarta.validation.constraints.NotNull

data class MemberSaveReq(
    @field:NotNull(message = "require emil")
    val email: String?,
    @field:NotNull(message = "require password")
    val password: String?,
    @field:NotNull(message = "require role")
    val role: Role?
)

fun MemberSaveReq.toEntity(): Member {
    return Member(email = this.email ?: "", password = this.password ?: "", role = this.role ?: Role.USER)
}

data class MemberRes(
    val id: Long,
    val email: String,
    val password: String,
    val role: Role
)
