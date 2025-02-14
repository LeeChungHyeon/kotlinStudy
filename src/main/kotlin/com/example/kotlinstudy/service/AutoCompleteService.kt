package com.example.kotlinstudy.service

import com.example.kotlinstudy.domain.post.PostRepository
import net.okihouse.autocomplete.repository.AutocompleteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AutoCompleteService(
    private val autocompleteRepository: AutocompleteRepository,
    private val postRepository: PostRepository
) {

    @Transactional(readOnly = true)
    fun autoCompletePostTitle(word: String): MutableList<String> {

        postRepository.findAll().forEach {
            autocompleteRepository.add(it.title)
        }

        val complete = autocompleteRepository.complete(word)

        val searchWords = mutableListOf<String> ()

        for (autocompleteData in complete) {
            searchWords.add(autocompleteData.value)
        }
        return searchWords

    }

}