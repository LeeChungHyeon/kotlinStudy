package com.example.kotlinstudy.config.cache

import com.github.benmanes.caffeine.cache.Caffeine
import mu.KotlinLogging
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Arrays
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

@Configuration
@EnableCaching
class CacheConfig {

    private val log = KotlinLogging.logger {}


    @Bean
    fun cacheManager(): CacheManager {

        val cacheManager = SimpleCacheManager()

        val caches: List<CaffeineCache> = Arrays.stream(CacheType.values()).map { cache ->
            makeCaffeineCache(cache)
        }.collect(Collectors.toList())

        cacheManager.setCaches(caches)

        return cacheManager

    }

    private fun makeCaffeineCache(cache: CacheType): CaffeineCache {
        val caffeineCache = CaffeineCache(
            cache.cacheName, Caffeine.newBuilder().recordStats()
                .expireAfterWrite(cache.expriedAfterWrite, TimeUnit.SECONDS)
                .maximumSize(cache.maximumSize)
                .removalListener { key: Any?, value: Any?, cause ->
                    log.info { "key $key was evicted $cause: $value" }
                }
                .build()
        )
        return caffeineCache
    }

}