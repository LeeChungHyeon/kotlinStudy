package com.example.kotlinstudy.api

import com.example.kotlinstudy.domain.member.Member
import com.example.kotlinstudy.service.MemberService
import com.example.kotlinstudy.util.value.CmResDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/members")
    fun findAll(): CmResDto<*> {
        return CmResDto(HttpStatus.OK, "find All Members", memberService.findAll())
    }

}