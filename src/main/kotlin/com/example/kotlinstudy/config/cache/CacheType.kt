package com.example.kotlinstudy.config.cache

enum class CacheType(
    val cacheName: String,
    val expriedAfterWrite: Long,
    val maximumSize: Long,
    val cacheKey: String
) {

    POST_NAME("postName", 100, 10_000, "postTitleKey");

    companion object {

        const val postName = "postName"

    }

}