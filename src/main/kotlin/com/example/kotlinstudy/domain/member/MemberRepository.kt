package com.example.kotlinstudy.domain.member

import com.linecorp.kotlinjdsl.query.spec.ExpressionOrderSpec
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository

interface MemberRepository : JpaRepository<Member, Long>, MemberCustomRepository {

}

interface MemberCustomRepository {

    fun findMembers(pageable: Pageable): Page<Member>
    fun findMemberByEmail(email: String): Member

}

class MemberCustomRepositoryImpl(
    private val queryFactory: SpringDataQueryFactory
) : MemberCustomRepository {


    val log = KotlinLogging.logger {}

    override fun findMembers(pageable: Pageable): Page<Member> {
        val results = queryFactory.listQuery<Member> {
            select(entity(Member::class))
            from(entity(Member::class))
            limit(pageable.pageSize)
            offset(pageable.offset.toInt())
            orderBy(ExpressionOrderSpec(column(Member::id), false))
        }

        val countQuery = queryFactory.listQuery<Member> {
            select(entity(Member::class))
            from(entity(Member::class))
        }

        return PageableExecutionUtils.getPage(results, pageable) {
            countQuery.size.toLong()
        }
    }

    override fun findMemberByEmail(email: String): Member {
        return queryFactory.singleQuery<Member> {
            select(entity(Member::class))
            from(entity(Member::class))
            where(
                column(Member::email).equal(email)
            )
        }
    }
}