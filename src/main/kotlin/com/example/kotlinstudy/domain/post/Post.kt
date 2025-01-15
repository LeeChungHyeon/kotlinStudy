package com.example.kotlinstudy.domain.post

import com.example.kotlinstudy.domain.AuditingEntity
import jakarta.persistence.*

@Entity
@Table(name = "Post")
class Post(
    title: String,
    content: String
) : AuditingEntity() {

    @Column(name = "title", nullable = false)
    var title: String = title
        protected set

    @Column(name = "content", nullable = true)
    var content: String = content
        protected set

}