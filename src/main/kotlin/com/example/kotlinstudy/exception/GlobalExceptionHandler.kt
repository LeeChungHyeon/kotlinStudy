package com.example.kotlinstudy.exception

import jakarta.persistence.EntityNotFoundException
import jakarta.persistence.NoResultException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    val log = KotlinLogging.logger {}

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        log.error { "handleMethodArgumentNotValidException $e" }
        val of = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.bindingResult)

        return ResponseEntity(of, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(e: BusinessException): ResponseEntity<ErrorResponse> {
        log.error { "handleEntityNotFoundException $e" }
        val of = ErrorResponse.of(e.errorCode)

        return ResponseEntity(of, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(NoResultException::class)
    fun handleNoResultException(e: EntityNotFoundException): ResponseEntity<ErrorResponse> {
        log.error { "NoResultException $e" }
        val of = ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND)
        return ResponseEntity(of, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}