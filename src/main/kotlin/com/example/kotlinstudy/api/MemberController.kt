package com.example.kotlinstudy.api

import com.example.kotlinstudy.domain.member.Member
import com.example.kotlinstudy.domain.member.MemberSaveReq
import com.example.kotlinstudy.service.MemberService
import com.example.kotlinstudy.util.value.CmResDto
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.io.Serializable

@RestController
class MemberController(
    private val memberService: MemberService
) {

    @GetMapping("/members")
    fun findAll(@PageableDefault(size = 10) pageable: Pageable): CmResDto<*> {
        return CmResDto(HttpStatus.OK, "find All Members", memberService.findAll(pageable))
    }

    @GetMapping("/member/{id}")
    fun findById(@PathVariable id: Long): CmResDto<Any> {
        return CmResDto(HttpStatus.OK, "find Member by Id", memberService.findMemberById(id))
    }

    @DeleteMapping("/member/{id}")
    fun delete(@PathVariable id: Long): CmResDto<Any> {
        return CmResDto(HttpStatus.OK, "delete Member by Id", memberService.deleteMember(id))
    }

    @PostMapping("/member")
    fun save(@Valid @RequestBody dto: MemberSaveReq): CmResDto<*> {
        return CmResDto(HttpStatus.OK, "save Member", memberService.saveMember(dto))
    }

}