package com.example.kotlinstudy.service

import com.example.kotlinstudy.config.cache.CacheType
import com.example.kotlinstudy.domain.post.PostRepository
import mu.KotlinLogging
import net.okihouse.autocomplete.implement.AutocompleteServiceImpl
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class CacheService(
    private val postRepository: PostRepository,
    private val autocompleteRepository: AutocompleteServiceImpl
) {

    private val log = KotlinLogging.logger {}

    @Cacheable(cacheNames = [CacheType.postName], key = "#result == T(com.example.kotlinstudy.config.cache.CacheType).POST_NAME.cacheKey")
    fun addAutoCompletePostTitle(): AutocompleteServiceImpl {
        postRepository.findAll().forEach {
            autocompleteRepository.add(it.title)
        }
        return autocompleteRepository
    }

}