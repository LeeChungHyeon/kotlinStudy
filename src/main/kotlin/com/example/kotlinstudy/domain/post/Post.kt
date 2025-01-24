package com.example.kotlinstudy.domain.post

import com.example.kotlinstudy.domain.AuditingEntity
import com.example.kotlinstudy.domain.member.Member
import jakarta.persistence.*

@Entity
@Table(name = "Post")
class Post(
    id: Long = 0,
    title: String,
    content: String,
    member: Member,
) : AuditingEntity(id) {

    @Column(name = "title", nullable = false)
    var title: String = title
        protected set

    @Column(name = "content", nullable = true, length = 5000)
    var content: String = content
        protected set

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Member::class)
    var member: Member = member
        protected set

    override fun toString(): String {
        return "Post(id=$id, title='$title', content='$content', member=$member)"
    }
}

fun Post.toDto(): PostRes {
    return PostRes(
        id = this.id!!,
        title = this.title,
        content = this.content,
        member = this.member.toDto()
    )
}