package com.example.kotlinstudy.config.aop

import mu.KotlinLogging

object CustomAopObject {

    private val log = KotlinLogging.logger {}

    fun highOrder(func:()->Unit) {
        log.info {"before"}

        log.info {"after"}
    }

}

