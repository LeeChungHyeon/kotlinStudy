package com.example.kotlinstudy.domain.comment

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(name = "Comment_closure")
class CommentClosure(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    idAncestor: Comment? = null,
    idDescendant: Comment,
    depth: Int = 0,
    createAt: LocalDateTime = LocalDateTime.now(),
    updateAt: LocalDateTime = LocalDateTime.now()
) {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ancestor", nullable = true)
    var idAncestor = idAncestor
        protected set


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_descendant", nullable = false)
    var idDescendant = idDescendant
        protected set

    @Column(name = "depth")
    var depth = depth
        protected set

    @CreatedDate
    @Column(name = "create_at", nullable = false, updatable = false)
    var createAt: LocalDateTime = createAt
        protected set

    @LastModifiedDate
    @Column(name = "update_at")
    var updateAt: LocalDateTime = updateAt
        protected set

    override fun toString(): String {
        return "CommentClosure(id = $id, idAncestor = $idAncestor, idDescendant = $idDescendant, depth = $depth)"
    }
}