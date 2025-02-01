package com.example.kotlinstudy.config.aop

import com.example.kotlinstudy.util.value.CmResDto
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice
class RestVersionAdvice<T>(

) : ResponseBodyAdvice<T> {

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(
        body: T?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): T? {
        if (body is CmResDto<*>) {
            val apiVersion = request.uri.path.split("/").first { it.isNotEmpty() }

            body.reflectVersion(apiVersion)

        }

        return body
    }
}