package com.example.kotlinstudy.config.security

import mu.KotlinLogging
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import java.util.*
import java.util.concurrent.TimeUnit

class JwtManager(
    accessTokenExpireSecond: Long = 60, // 1분
    refreshTokenExpireDay: Long = 7
) {

    private val log = KotlinLogging.logger {}

    private val accessSecretKey: String = "myAccessSecretKey"
    private val refreshSecretKey: String = "myRefreshSecretKey"
     val claimPrincipal = "principal"
    private val accessTokenExpireSecond: Long = accessTokenExpireSecond
    val refreshTokenExpireDay: Long = refreshTokenExpireDay
    val authorizationHeader = "Authorization"
    val jwtHeader = "Bearer "
    private val jwtSubject = "my-token"


    fun generateAccessToken(principal: String): String {
        val expireDate = Date(System.nanoTime() + TimeUnit.SECONDS.toMillis(accessTokenExpireSecond))

        log.info { "accessToken Expire: $expireDate" }

        return doGenerateToken(expireDate, principal, accessSecretKey)
    }

    fun generateRefreshToken(principal: String): String {
        val expireDate = Date(System.nanoTime() + TimeUnit.DAYS.toMillis(refreshTokenExpireDay))

        log.info { "refreshToken Expire: $expireDate" }

        return doGenerateToken(expireDate, principal, refreshSecretKey)
    }

    private fun doGenerateToken(expireDate: Date, principal: String, secretKey: String) = JWT.create()
        .withSubject(jwtSubject)
        .withExpiresAt(expireDate)
        .withClaim(claimPrincipal, principal)
        .sign(Algorithm.HMAC512(secretKey))

    fun getPrincipalStringByAccessToken(accessToken: String): String {
        val tokenResult = getDecodedJWT(secretKey = accessSecretKey, token = accessToken)

        return tokenResult.getClaim(claimPrincipal).asString()
    }

    fun getPrincipalStringByRefreshToken(refreshToken: String): String {
        val tokenResult = getDecodedJWT(secretKey = refreshSecretKey, token = refreshToken)

        return tokenResult.getClaim(claimPrincipal).asString()
    }

    private fun getDecodedJWT(secretKey: String, token: String): DecodedJWT {
        val verifier: JWTVerifier = JWT.require(Algorithm.HMAC512(secretKey)).build()
        val decodedJWT: DecodedJWT = verifier.verify(token)

        return decodedJWT
    }

    fun validAccessToken(token: String): TokenValidResult {
        return validatedJwt(token, accessSecretKey)
    }

    fun validRefreshToken(token: String): TokenValidResult {
        return validatedJwt(token, refreshSecretKey)
    }

    private fun validatedJwt(token: String, secretKey: String): TokenValidResult {
        try {
            getDecodedJWT(secretKey, token)

            return TokenValidResult.Success()
        } catch (e: JWTVerificationException) {
            log.error("error = ${e.stackTraceToString()}")

            return TokenValidResult.Failure(e)
        }
    }

    companion object {
        fun getRefreshTokenDay(): Long {
            val jwtManager = JwtManager()

            return jwtManager.refreshTokenExpireDay
        }
    }


}

sealed class TokenValidResult {
    class Success(val successValue: Boolean = true) : TokenValidResult()
    class Failure(val exception: JWTVerificationException) : TokenValidResult()
}