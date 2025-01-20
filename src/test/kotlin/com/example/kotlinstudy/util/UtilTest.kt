package com.example.kotlinstudy.util

import com.example.kotlinstudy.config.security.JwtManager
import com.example.kotlinstudy.config.security.PrincipalDetails
import com.example.kotlinstudy.domain.member.Member
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class UtilTest {

    private val log = KotlinLogging.logger {}

    @Test
    fun errorLogTest() {
        log.error { "error" }
    }

    @Test
    fun generateJwtTest() {
        val jwtManager = JwtManager()

        val details = PrincipalDetails(Member.createFakeMember(1))
        val accessToken = jwtManager.generateAccessToken(details)

        val email = jwtManager.getMemberEmail(accessToken)

        log.info { "accessToken: $accessToken" }
        log.info { "email: $email" }
    }

    @Test
    fun bcryptEncoderTest() {
        val encoder = BCryptPasswordEncoder()

        val encpassword = encoder.encode("1234")

        log.info { "bcrypt password: $encpassword" }

    }
}