package com.example.kotlinstudy.config

import com.example.kotlinstudy.domain.member.*
import com.example.kotlinstudy.domain.post.Post
import com.example.kotlinstudy.domain.post.PostRepository
import com.example.kotlinstudy.domain.post.PostSaveReq
import io.github.serpro69.kfaker.faker
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class InitData(
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository,
) {

    val faker = faker {  }

    private val log = KotlinLogging.logger {}

    @EventListener(ApplicationReadyEvent::class)
    private fun init() {

//        val members = generateMembers(20)
//        memberRepository.saveAll(members)
//
//        val posts = generatePosts(20)
//        postRepository.saveAll(posts)



    }

    private fun generateMembers(cnt: Int): MutableList<Member> {
        val members = mutableListOf<Member>()

        for (i in 1..cnt) {
            val member = generateMember()
            log.info { "insert $member" }
            memberRepository.save(member)
        }
        return members
    }

    private fun generatePosts(cnt: Int): MutableList<Post> {
        val posts = mutableListOf<Post>()

        for (i in 1..cnt) {
            val post = generatePost()
            log.info { "insert $post" }
            posts.add(post)
        }
        return posts
    }


    private fun generateMember(): Member =
        LoginDto(
            email = faker.internet.safeEmail(),
            rawPassword = "1234",
            role = Role.USER
        ).toEntity()

    private fun generatePost(): Post =
        PostSaveReq(
            title = faker.theExpanse.ships(),
            content = faker.quote.matz(),
            memberId = 1
        ).toEntity()

}