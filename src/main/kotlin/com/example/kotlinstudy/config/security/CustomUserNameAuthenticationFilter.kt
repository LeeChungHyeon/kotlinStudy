package com.example.kotlinstudy.config.security

import com.example.kotlinstudy.domain.InMemoryRepository
import com.example.kotlinstudy.domain.member.LoginDto
import com.example.kotlinstudy.util.CookieProvider
import com.example.kotlinstudy.util.CookieProvider.CookieName
import com.example.kotlinstudy.util.Script
import com.example.kotlinstudy.util.value.CmResDto
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.concurrent.TimeUnit

class CustomUserNameAuthenticationFilter(
    private val objectMapper: ObjectMapper,
    private val memoryRepository: InMemoryRepository,
) : UsernamePasswordAuthenticationFilter() {

    private val log = KotlinLogging.logger {}
    private val jwtManager = JwtManager()

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {

        log.debug { "login 요청" }

        lateinit var loginDto: LoginDto

        try {
            loginDto = objectMapper.readValue(request?.inputStream, LoginDto::class.java)
            log.debug { "login Dto: $loginDto" }
        } catch (e: Exception) {
            log.error { "loginFilter: 로그인 요청 DTO 생성 중 실패 $e" }
        }

        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.email, loginDto.rawPassword)

        return this.authenticationManager.authenticate(authenticationToken)

    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {

        log.debug { "로그인 성공 후 JWT 토근 response" }

        val principalDetails = authResult?.principal as PrincipalDetails

        val accessToken = jwtManager.generateAccessToken(objectMapper.writeValueAsString(principalDetails))
        val refreshToken = jwtManager.generateRefreshToken(objectMapper.writeValueAsString(principalDetails))

        val refreshCookie = CookieProvider.createCookie(
            CookieName.REFRESH_COOKIE,
            refreshToken,
            TimeUnit.DAYS.toSeconds(jwtManager.refreshTokenExpireDay)
        )

        response?.addHeader(jwtManager.authorizationHeader, jwtManager.jwtHeader + accessToken)
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString())
        memoryRepository.save(refreshToken, objectMapper.writeValueAsString(principalDetails))

        val jsonResult = objectMapper.writeValueAsString(CmResDto(HttpStatus.OK, "login success", principalDetails.member))
        Script.responseData(response, jsonResult)

    }
}