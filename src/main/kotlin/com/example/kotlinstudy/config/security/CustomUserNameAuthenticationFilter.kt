package com.example.kotlinstudy.config.security

import com.example.kotlinstudy.domain.member.LoginDto
import com.example.kotlinstudy.util.value.CmResDto
import com.example.kotlinstudy.util.value.func.responseData
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class CustomUserNameAuthenticationFilter(
    private val objectMapper: ObjectMapper
) : UsernamePasswordAuthenticationFilter() {

    private val log = KotlinLogging.logger {}
    private val jwtManager = JwtManager()

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {

        log.info { "login 요청" }

        lateinit var loginDto: LoginDto

        try {
            loginDto = objectMapper.readValue(request?.inputStream, LoginDto::class.java)
            log.info { "login Dto: $loginDto" }
        } catch (e: Exception) {
            log.error { "loginFilter: 로그인 요청 DTO 생성 중 실패 $e" }
        }

        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.email, loginDto.password)

        return this.authenticationManager.authenticate(authenticationToken)

    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {

        log.info { "로그인 성공 후 JWT 토근 response" }

        val principalDetails = authResult?.principal as PrincipalDetails
        val jwtToken = jwtManager.generateAccessToken(objectMapper.writeValueAsString(principalDetails))
        response?.addHeader(jwtManager.authorizationHeader, jwtManager.jwtHeader + jwtToken)

        val jsonResult = objectMapper.writeValueAsString(CmResDto(HttpStatus.OK, "login success", principalDetails.member))
        responseData(response, jsonResult)

    }
}