package com.example.kotlinstudy.config.redis

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import redis.embedded.RedisServer

@Configuration
@Profile("local")
class EmbeddedRedisConfig(

) {

    private val log = KotlinLogging.logger {}

    lateinit var redisServer: RedisServer

    @Value("\${spring.redis.port}")
    val port: Int = 6379

    @Value("\${spring.redis.host}")
    val host: String = "localhost"

    @PostConstruct
    fun init() {

        log.debug { "embedded redis start port: $port" }
        log.debug { "embedded redis start host: $host" }

        this.redisServer = RedisServer()
        redisServer.start()
    }

    @PreDestroy
    fun destroy() {
        log.debug { "embedded redis stop" }

        this.redisServer.stop()
    }

}