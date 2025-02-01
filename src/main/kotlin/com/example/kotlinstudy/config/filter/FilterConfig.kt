package com.example.kotlinstudy.config.filter

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered

@Configuration
class FilterConfig {

    @Bean
    fun mdcLoggingFilter(): FilterRegistrationBean<MDCLoggingFilter> {
        val bean = FilterRegistrationBean(MDCLoggingFilter())

        bean.addUrlPatterns("/*")
        bean.order = Ordered.HIGHEST_PRECEDENCE

        return bean
    }

}