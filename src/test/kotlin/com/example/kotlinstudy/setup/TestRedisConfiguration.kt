package com.example.kotlinstudy.setup

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer

@TestConfiguration
class TestRedisConfiguration {

    private val redisServer: RedisServer = RedisServer(6379)


//    @PostConstruct
//    fun postConstruct() {
//        redisServer.start()
//    }
//
//    @PreDestroy
//    fun preDestroy() {
//        redisServer.stop()
//    }
}