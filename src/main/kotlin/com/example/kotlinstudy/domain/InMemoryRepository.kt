package com.example.kotlinstudy.domain

import java.util.*
import kotlin.collections.ArrayList

interface InMemoryRepository {
    fun clear()
    fun remove(key: String): Any?
    fun findAll(): ArrayList<Any>
    fun findByKey(key: String): Any
    fun save(key: String, value: Any)
}