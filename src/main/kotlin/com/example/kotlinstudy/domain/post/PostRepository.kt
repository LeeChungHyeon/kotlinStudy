package com.example.kotlinstudy.domain.post

import com.example.kotlinstudy.domain.member.Member
import com.linecorp.kotlinjdsl.query.spec.ExpressionOrderSpec
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.querydsl.from.fetch
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

interface PostRepository : JpaRepository<Post, Long>, PostCustomRepository {
}

interface PostCustomRepository {

    fun findPosts(pageable: Pageable): Page<Post>
}

class PostCustomRepositoryImpl(
    private val queryFactory: SpringDataQueryFactory
) : PostCustomRepository {

    override fun findPosts(pageable: Pageable): Page<Post> {
        val results = queryFactory.listQuery<Post> {
            select(entity(Post::class))
            from(entity(Post::class))
            fetch(Post::member)
            limit(pageable.pageSize)
            offset(pageable.offset.toInt())
            orderBy(ExpressionOrderSpec(column(Post::id), false))
        }

        val countQuery = queryFactory.listQuery<Post> {
            select(entity(Post::class))
            from(entity(Post::class))
        }

        return PageableExecutionUtils.getPage(results, pageable) {
            countQuery.size.toLong()
        }
    }

}