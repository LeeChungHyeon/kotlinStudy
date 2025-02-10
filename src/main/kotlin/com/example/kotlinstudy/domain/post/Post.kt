package com.example.kotlinstudy.domain.post

import com.example.kotlinstudy.domain.member.Member
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(name = "Post")
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    title: String,
    content: String,
    member: Member,
    createAt: LocalDateTime,
    updateAt: LocalDateTime
) {

    @Column(name = "title", nullable = false)
    var title: String = title
        protected set

    @Column(name = "content", nullable = true, length = 5000)
    var content: String = content
        protected set

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Member::class)
    var member: Member = member
        protected set

    @CreatedDate
    @Column(name = "create_at", nullable = false, updatable = false)
    var createAt: LocalDateTime = LocalDateTime.now()
        protected set

    @LastModifiedDate
    @Column(name = "update_at")
    var updateAt: LocalDateTime = LocalDateTime.now()
        protected set


    fun toDto(): PostRes {
        return PostRes(
            id = this.id!!,
            title = this.title,
            content = this.content,
            member = this.member.toDto()
        )
    }

    override fun toString(): String {
        return "Post(id=$id, title='$title', content='$content', member=$member)"
    }



}

