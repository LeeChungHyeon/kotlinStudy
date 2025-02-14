package com.example.kotlinstudy.config.redis

import net.okihouse.autocomplete.implement.AutocompleteServiceImpl
import net.okihouse.autocomplete.key.AutocompleteKeyServiceImpl
import net.okihouse.autocomplete.repository.AutocompleteKeyRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.StringRedisTemplate

@Configuration
class AutocompleeteConfig(
    private val stringRedisTemplate: StringRedisTemplate
) {

    @Bean(name = ["autocompleteKeyRepository", "keyRepository"])
    fun keyRepository(): AutocompleteKeyRepository? {
        return AutocompleteKeyServiceImpl(stringRedisTemplate)
    }

    @Bean(name = ["autocompleteRepository"])
    fun autocompleteRepository(autocompleteKeyRepository: AutocompleteKeyRepository): AutocompleteServiceImpl {
        return AutocompleteServiceImpl(stringRedisTemplate, autocompleteKeyRepository)
    }

}