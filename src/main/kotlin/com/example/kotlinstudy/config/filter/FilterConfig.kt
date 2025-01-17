package com.example.kotlinstudy.config.filter

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    @Bean
    fun registMyAuthentionFilter(): FilterRegistrationBean<MyAuthentionfilter> {
        val bean = FilterRegistrationBean(MyAuthentionfilter())

        bean.addUrlPatterns("/api/*")
        bean.order = 0

        return bean
    }

}