package com.example.kotlinstudy.domain.comment

import com.example.kotlinstudy.domain.member.Member
import com.example.kotlinstudy.domain.post.Post
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(name = "Comment")
class Comment(
    content: String,
    post: Post,
    member: Member,
    createAt: LocalDateTime = LocalDateTime.now(),
    updateAt: LocalDateTime = LocalDateTime.now()
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
        protected set

    @Column(name = "content", nullable = false, length = 1000)
    var content: String = content
        protected set

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Post::class)
    var post: Post = post
        protected set

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Member::class)
    var member: Member = member
        protected set

    @CreatedDate
    @Column(name = "create_at", nullable = false, updatable = false)
    var createAt: LocalDateTime = createAt
        protected set

    @LastModifiedDate
    @Column(name = "update")
    var updateAt: LocalDateTime = updateAt
        protected set


    fun toDto(): CommentRes {
        val dto = CommentRes(
            member = this.member.toDto(),
            content = this.content
        )

        return dto

    }

    override fun toString(): String {
        return "Comment(id = $id, content = $content, post = $post, member = $member)"
    }
}