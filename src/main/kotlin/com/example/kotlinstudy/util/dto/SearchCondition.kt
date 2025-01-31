package com.example.kotlinstudy.util.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

class SearchCondition(
    @JsonProperty("searchType") val searchType: SearchType?,
    @JsonProperty("keyword") val keyword: String?
) {



}

enum class SearchType {
    EMAIL, TITLE, CONTENT;

    companion object {
        @JsonCreator
        fun from(s: String?): SearchType? {
            return SearchType.valueOf(s?.uppercase().toString())
        }
    }


}