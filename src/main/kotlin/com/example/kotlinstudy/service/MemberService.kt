package com.example.kotlinstudy.service

import com.example.kotlinstudy.domain.member.Member
import com.example.kotlinstudy.domain.member.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {

    @Transactional(readOnly = true)
    fun findAll(): MutableList<Member> {
        return memberRepository.findAll()
    }

}