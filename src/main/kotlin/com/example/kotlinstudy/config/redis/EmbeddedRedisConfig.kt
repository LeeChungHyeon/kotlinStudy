package com.example.kotlinstudy.config.redis

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ClassPathResource
import redis.embedded.RedisServer
import java.io.File
import java.util.*


@Configuration
@Profile("local")
class EmbeddedRedisConfig(

) {

    private val log = KotlinLogging.logger {}

    lateinit var redisServer: RedisServer

    @Value("\${spring.data.redis.port}")
    val port: Int = 6379

    @PostConstruct
    fun init() {

        log.debug { "embedded redis start port: $port" }
        //this.redisServer = RedisServer(getRedisFileForArcMac(), this.port)
        //redisServer.start()

        this.redisServer = if (isArmMac()) {
            RedisServer(getRedisFileForArcMac(), this.port)
        } else {
            RedisServer.builder()
                .port(port)
                .build()
        }

        try {
            redisServer.start()
        } catch (e: java.lang.Exception) {
            throw RuntimeException(e.message)
        }

    }

    @PreDestroy
    fun destroy() {
        log.debug { "embedded redis stop" }

        this.redisServer.stop()
    }

    private fun isArmMac(): Boolean {
        return Objects.equals(System.getProperty("os.arch"), "aarch64")
                && Objects.equals(System.getProperty("os.name"), "Mac OS X")
    }

    /**
     * ARM 아키텍처를 사용하는 Mac에서 실행할 수 있는 Redis 바이너리 파일을 반환
     */
    private fun getRedisFileForArcMac(): File {
        try {
            return ClassPathResource("redis/redis-server-7.2.3-mac-arm64").file
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        }
    }

}