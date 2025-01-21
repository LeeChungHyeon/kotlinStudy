package com.example.kotlinstudy.config.security

import mu.KotlinLogging
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtManager {

    private val log = KotlinLogging.logger {}

    private val secritKey: String = "asdfasdf"
    private val claimEmail = "email"
    private val claimPassword = "password"
    private val expireTime = 1000 * 60 * 60
    val authorizationHeader = "Authorization"
    val jwtHeader = "Bearer "


    fun generateAccessToken(principal: PrincipalDetails): String {
        return JWT.create()
            .withSubject(principal.username)
            .withExpiresAt(Date(System.nanoTime() + 1000*60*60))
            .withClaim(claimEmail, principal.username)
            .withClaim(claimPassword, principal.password)
            .sign(Algorithm.HMAC512(secritKey))
    }

    fun getMemberEmail(token: String): String? {
        return JWT.require(Algorithm.HMAC512(secritKey)).build().verify(token).getClaim(claimEmail).asString()
    }

}