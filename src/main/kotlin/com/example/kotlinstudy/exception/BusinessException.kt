package com.example.kotlinstudy.exception

sealed class BusinessException : RuntimeException {

    val errorCode: ErrorCode

    constructor(errorCode: ErrorCode): super(errorCode.message) {
        this.errorCode = errorCode
    }

    constructor(message: String?, errorCode: ErrorCode): super(message) {
        this.errorCode = errorCode
    }



}