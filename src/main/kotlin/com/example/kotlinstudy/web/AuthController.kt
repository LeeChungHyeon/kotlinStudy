package com.example.kotlinstudy.web

import com.example.kotlinstudy.domain.member.LoginDto
import com.example.kotlinstudy.service.AuthService
import com.example.kotlinstudy.util.value.CmResDto
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("/auth")
@RestController
class AuthController(
    private val authService: AuthService,
) {

    val log = KotlinLogging.logger {}


    @PostMapping("/login")
    fun login(session: HttpSession) {
        session.setAttribute("principal", "pass")
    }

    @PostMapping("/member")
    fun joinApp(@Valid @RequestBody dto: LoginDto): CmResDto<*> {
        log.info { "JoinApp Request: $dto" }
        return CmResDto(HttpStatus.OK, "회원가입", authService.saveMember(dto))
    }

}