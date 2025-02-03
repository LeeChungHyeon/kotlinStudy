package com.example.kotlinstudy.domain.member

import com.example.kotlinstudy.config.BeanAccesseor
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

data class LoginDto(
    @field:NotNull(message = "require emil")
    val email: String?,
    val rawPassword: String?,
    val role: Role?
) {
    fun toEntity(): Member {
        return Member(
            email = this.email ?: "",
            password = encodeRawPassword(),
            role = this.role ?: Role.USER,
            createAt = LocalDateTime.now(),
            updateAt = LocalDateTime.now()
        )
    }

    private fun encodeRawPassword(): String =
        BeanAccesseor.getBean(PasswordEncoder::class).encode(this.rawPassword)


}

data class MemberRes(
    val id: Long,
    val email: String,
    val password: String,
    val role: Role,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    val updatedAt: LocalDateTime,
)
