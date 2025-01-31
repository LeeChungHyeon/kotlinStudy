package com.example.kotlinstudy.repo

import com.example.kotlinstudy.config.P6spyPrettySqlFormatter
import com.example.kotlinstudy.domain.post.PostRepository
import com.example.kotlinstudy.util.dto.SearchCondition
import com.example.kotlinstudy.util.dto.SearchType
import com.linecorp.kotlinjdsl.query.creator.CriteriaQueryCreatorImpl
import com.linecorp.kotlinjdsl.query.creator.SubqueryCreatorImpl
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactoryImpl
import com.p6spy.engine.spy.P6SpyOptions
import jakarta.persistence.EntityManager
import mu.KotlinLogging
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional


@DataJpaTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryesTest {

    val log = KotlinLogging.logger {}


    @Autowired
    private lateinit var postRepository: PostRepository


    @Test
    fun setupTest() {
        log.info { "setup" }
    }

    @Test
    fun jdslDynamicQueryTest() {
        val (pageable, condition) = pageRequestSearchConditionPair()

        val posts = postRepository.findPosts(pageable = pageable, searchCondition = condition).content

        for (post in posts) {
            log.info { "post : $post" }
        }

        Assertions.assertThat(posts.size).isEqualTo(6)
    }

    private fun pageRequestSearchConditionPair(): Pair<PageRequest, SearchCondition> {
        val pageable = PageRequest.of(0, 100)
        val condition = SearchCondition(
            searchType = SearchType.TITLE,
            keyword = "kot"
        )
        return Pair(pageable, condition)
    }


    @TestConfiguration
    class Testconfiguration(
        @Autowired
        private val em: EntityManager
    ) {

        @Bean
        fun springDataQueryFactory(): SpringDataQueryFactory {

            P6SpyOptions.getActiveInstance().logMessageFormat = P6spyPrettySqlFormatter::class.java.name

            return SpringDataQueryFactoryImpl(
                criteriaQueryCreator = CriteriaQueryCreatorImpl(
                    em
                ),
                SubqueryCreatorImpl()
            )
        }


    }

}