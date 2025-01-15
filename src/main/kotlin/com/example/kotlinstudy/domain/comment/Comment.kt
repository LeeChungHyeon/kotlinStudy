package com.example.kotlinstudy.domain.comment

import com.example.kotlinstudy.domain.AuditingEntity
import com.example.kotlinstudy.domain.post.Post
import jakarta.persistence.*

@Entity
@Table(name = "Comment")
class Comment(
    content: String,
    post: Post,
) : AuditingEntity() {

    @Column(name = "content", nullable = false)
    var content: String = content
        protected set

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Post::class)
    var post: Post = post
        protected set

}