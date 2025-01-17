package com.example.kotlinstudy.service

import com.example.kotlinstudy.config.security.PrincipalDetails
import com.example.kotlinstudy.domain.member.MemberRepository
import com.example.kotlinstudy.exception.MemberNotFoundException
import mu.KotlinLogging
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberRepository: MemberRepository
) : UserDetailsService {

    val log = KotlinLogging.logger {}

    override fun loadUserByUsername(email: String): UserDetails {

        log.info { "loadUserByUsername 호출" }

        val member = memberRepository.findMemberByEmail(email)
        return PrincipalDetails(member)

    }
}