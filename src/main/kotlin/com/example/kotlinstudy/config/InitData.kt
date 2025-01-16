package com.example.kotlinstudy.config

import com.example.kotlinstudy.domain.member.*
import io.github.serpro69.kfaker.faker
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
class InitData(
    private val memberRepository: MemberRepository,
) {

    val faker = faker {  }

    private val log = KotlinLogging.logger {}

    @EventListener(ApplicationReadyEvent::class)
    private fun init() {
        val members = mutableListOf<Member>()

        for (i in 1..100) {
            val member = generateMember()
            log.info { "insert $member" }
            members.add(member)
        }


        memberRepository.saveAll(members)


    }




    private fun generateMember(): Member =
        MemberSaveReq(
            email = faker.internet.safeEmail(),
            password = "1234",
            role = Role.USER
        ).toEntity()

}