package com.example.kotlinstudy.api

import com.example.kotlinstudy.domain.member.Member
import com.example.kotlinstudy.service.MemberService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/members")
    fun findAll(): MutableList<Member> {
        return memberService.findAll()
    }

}