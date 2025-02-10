package com.example.kotlinstudy.domain.comment

import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.util.Assert

interface CommentRepository {

    fun saveComment(comment: Comment): Comment

    fun saveCommentClouser(idDescendant: Long, idAncestor: Long?): Int
}

@Repository
class CommentRepositoryImpl (
    private val queryFactory: SpringDataQueryFactory,
    private val em: EntityManager
) : CommentRepository {

    override fun saveComment(comment: Comment): Comment {
        Assert.notNull(comment, "Entity must not be null")
        return if (comment.id == 0L) {
            em.persist(comment)
            comment
        } else {
            em.merge(comment)
        }
    }

    override fun saveCommentClouser(idDescendant: Long, idAncestor: Long?): Int {

        var executeCount = 0

        val sql = """
            INSERT INTO Comment_closure
            (id_ancestor, id_descendant, depth, updated_at, created_at)
            VALUES
            ($idAncestor, $idDescendant, 0, now(), now())
        """.trimIndent()

        executeCount += em.createNativeQuery(sql).executeUpdate()

        if (idAncestor != null) {
            executeCount += em.createNativeQuery(
                """
                    INSERT INTO comment_closure
                    (id_ancestor, id_descendant, depth, updated_at, created_at)
                    select 
                    cc.id_ancestor,
                    c.id_descendant,
                    cc.depth + c.depth + 1,
                    c.updated_at
                    c.created_at
                    from comment_closure as cc, comment_closure as c 
                    where cc.id_descendant = $idAncestor and c.id_ancestor = $idDescendant
                """.trimIndent()
            ).executeUpdate()
        }

        return executeCount

    }

    

}

