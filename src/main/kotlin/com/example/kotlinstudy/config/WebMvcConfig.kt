package com.example.kotlinstudy.config

import com.example.kotlinstudy.util.dto.SearchType
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(

) : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(StringToEnumConverter())
    }
}

class StringToEnumConverter : Converter<String?, SearchType> {

    override fun convert(source: String): SearchType? {
        println("source == $source")

        return SearchType.valueOf(source.uppercase())
    }

}