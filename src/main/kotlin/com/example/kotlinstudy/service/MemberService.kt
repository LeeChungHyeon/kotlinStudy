package com.example.kotlinstudy.service

import com.example.kotlinstudy.domain.member.Member
import com.example.kotlinstudy.domain.member.MemberRepository
import com.example.kotlinstudy.domain.member.MemberRes
import com.example.kotlinstudy.domain.member.toDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable): Page<MemberRes> =
        memberRepository.findMembers(pageable).map {
            it.toDto()
        }


}