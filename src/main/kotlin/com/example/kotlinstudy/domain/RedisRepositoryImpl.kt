package com.example.kotlinstudy.domain

import com.example.kotlinstudy.config.security.JwtManager
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.util.Optional

open class RedisRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, Any>
) : InMemoryRepository {

    private val log = KotlinLogging.logger { }

    init {

    }



    override fun clear() {

        val keys = redisTemplate.keys("*")
        for (key in keys) {
            redisTemplate.delete(key)
        }

    }

    override fun remove(key: String): Any? {

        return redisTemplate.delete(key)

    }

    @Transactional
    override fun findAll(): MutableList<Any> {

        val keys = redisTemplate.keys("*")

        val list = mutableListOf<Any>()

        for (key in keys) {
            val map= mutableMapOf<String, Any>()
            val value = findByKey(key)

            //log.info { "key = $key   value = $value" }

            map[key] = value

            list.add(map)
        }
        return list
    }

    override fun findByKey(key: String): Any {

        return Optional.ofNullable(redisTemplate.opsForValue().get(key)).orElseThrow()

    }

    //@Transactional
    override fun save(key: String, value: Any) {

        //redisTemplate.opsForValue().set(key, value, Duration.ofDays(JwtManager.getRefreshTokenDay()))
        //log.info { "key = $key   value = $value" }

        Thread.sleep(50)

        redisTemplate.opsForValue().set(key, value)

    }
}