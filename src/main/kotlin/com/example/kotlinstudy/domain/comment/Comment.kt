package com.example.kotlinstudy.domain.comment

import com.example.kotlinstudy.domain.AuditingEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "Comment")
class Comment(
    content: String,
) : AuditingEntity() {

    @Column(name = "content", nullable = false)
    var content: String = content
        protected set

}