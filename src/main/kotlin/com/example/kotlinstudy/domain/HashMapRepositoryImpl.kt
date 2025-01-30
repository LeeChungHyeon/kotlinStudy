package com.example.kotlinstudy.domain

import com.example.kotlinstudy.config.security.JwtManager
import mu.KotlinLogging
import net.jodah.expiringmap.ExpirationPolicy
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList
import net.jodah.expiringmap.ExpiringMap
import java.util.concurrent.TimeUnit

open class HashMapRepositoryImpl(

) : InMemoryRepository {
    private val log = KotlinLogging.logger {}

    //private val store = ConcurrentHashMap<String, Any>()
    private val store: MutableMap<String, Any> = ExpiringMap.builder()
        .expiration(JwtManager.getRefreshTokenDay(), TimeUnit.DAYS)
        .expirationPolicy(ExpirationPolicy.CREATED)
        .expirationListener { key: String, value: Any ->
            log.info { "key: $key value: $value expired" }
        }
        .maxSize(10000000)
        .build()

    override fun clear() {
        store.clear()
    }

    override fun remove(key: String): Any? {
        return store.remove(key)
    }

    override fun findAll(): MutableList<Any> {
        return ArrayList<Any>(store.values)
    }

    override fun findByKey(key: String): Any {
        return Optional.ofNullable(store.get(key)).orElseThrow {
            throw RuntimeException("not found refreshToken")
        }
    }

    override fun save(key: String, value: Any) {
        Thread.sleep(50)
        store[key] = value
    }
}