package com.example.kotlinstudy.util.value

data class CmResDto<T> (
    val resultCode: T,
    val resultMsg: String,
    val data: T
)