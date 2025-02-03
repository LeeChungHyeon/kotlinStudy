package com.example.kotlinstudy.domain.post

import com.example.kotlinstudy.domain.member.Member
import com.example.kotlinstudy.domain.member.MemberRes
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class PostSaveReq(
    @field:NotNull(message = "require title")
    val title: String?,
    @field:NotNull(message = "require content")
    val content: String?,
    @field:NotNull(message = "require memberId")
    val memberId: Long?,
) {
    fun toEntity(): Post {
        return Post(
            title = this.title ?: "",
            content = this.content ?: "",
            member = Member.createFakeMember(this.memberId!!),
            createAt = LocalDateTime.now(),
            updateAt = LocalDateTime.now()
        )
    }

}



data class PostRes(
    val id: Long,
    val title: String,
    val content: String,
    val member: MemberRes
)