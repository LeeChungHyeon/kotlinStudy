package com.example.kotlinstudy.domain.member

import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class LoginDto(
    @field:NotNull(message = "require emil")
    val email: String?,
    @field:NotNull(message = "require password")
    val password: String?,
    @field:NotNull(message = "require role")
    val role: Role?
)

fun LoginDto.toEntity(): Member {
    return Member(email = this.email ?: "", password = this.password ?: "", role = this.role ?: Role.USER)
}

data class MemberRes(
    val id: Long,
    val email: String,
    val password: String,
    val role: Role,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
