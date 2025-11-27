package org.sw.spring.common

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.sw.spring.common.exception.CommonException
import java.net.URI

@RestControllerAdvice
class GlobalExceptionHandler {

    companion object {
        private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

    @ExceptionHandler(CommonException::class)
    fun handleCommonExceptions(
        servlet: HttpServletRequest,
        cause: CommonException
    ): ResponseEntity<ProblemDetail> {
        return ProblemDetail.forStatus(cause.status).apply {
            type = URI.create(servlet.requestURI)
            title = cause.title
            detail = cause.detail
            instance = URI.create(cause.instance)
        }.let {
            ResponseEntity.status(it.status)
                .header("Content-Type", "application/problem+json")
                .body(it)
        }
    }

    @ExceptionHandler(Exception::class)
    fun handleExceptions(
        servlet: HttpServletRequest,
        cause: Exception
    ): ResponseEntity<ProblemDetail> {
        return ProblemDetail.forStatus(500).apply {
            type = URI.create(servlet.requestURI)
            title = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
            detail = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
            instance = null
        }.let {
            ResponseEntity.status(it.status)
                .header("Content-Type", "application/problem+json")
                .body(it)
        }
    }




}
