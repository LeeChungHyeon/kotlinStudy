package com.example.kotlinstudy.web

import com.example.kotlinstudy.service.AutoCompleteService
import com.example.kotlinstudy.util.dto.SearchCondition
import com.example.kotlinstudy.util.dto.SearchType
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
    private val autoCompleteService: AutoCompleteService
) {

    private val log = KotlinLogging.logger {}

    @GetMapping("/autocomplete")
    fun autoCompleteTest(@RequestParam word: String): MutableList<String> {
        return autoCompleteService.autoCompletePostTitle(word)
    }


    @GetMapping("/health")
    fun healthTest(): String {
        return "hello kotlinStudy"
    }

    @GetMapping("/enum")
    fun enumTest(searchCondition: SearchCondition): String {
        log.info {
            """
            $searchCondition
            
            ${searchCondition.searchType}
            ${searchCondition.keyword}
            """.trimIndent()
        }

        return "test"
    }

    @GetMapping("/enum2")
    fun enumTest2(searchType: SearchType): String {
        return searchType.name
    }


}