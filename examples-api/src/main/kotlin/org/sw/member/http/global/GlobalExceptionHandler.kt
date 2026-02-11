package org.sw.member.http.global

import org.slf4j.LoggerFactory
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.sw.member.domain.config.AppProperties
import org.sw.member.domain.exception.CommonException
import org.sw.member.domain.exception.ErrorCode
import org.sw.member.domain.logging.LogMarker
import java.net.URI
import java.time.Instant

@RestControllerAdvice
class GlobalExceptionHandler(
    private val appProperties: AppProperties
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(CommonException::class)
    fun handleCommonException(e: CommonException): ProblemDetail {
        log.error(LogMarker.ERROR, "Common exception: {}", e.message, e)
        return buildProblemDetail(e.errorCode, e.message)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(e: MethodArgumentTypeMismatchException): ProblemDetail {
        log.error(LogMarker.ERROR, "Type mismatch: {}", e.message, e)
        return when {
            e.parameter.hasParameterAnnotation(PathVariable::class.java) -> handlePathVariable(e)
            e.parameter.hasParameterAnnotation(RequestParam::class.java) -> handleRequestParam(e)
            else -> buildProblemDetail(ErrorCode.INVALID_PARAMETER, e.message)
        }
    }

    private fun handlePathVariable(e: MethodArgumentTypeMismatchException): ProblemDetail {
        return buildProblemDetail(ErrorCode.INVALID_PARAMETER, "Path variable '${e.name}' type mismatch: ${e.value}")
    }

    private fun handleRequestParam(e: MethodArgumentTypeMismatchException): ProblemDetail {
        return buildProblemDetail(ErrorCode.INVALID_PARAMETER, "Request param '${e.name}' type mismatch: ${e.value}")
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ProblemDetail {
        log.error(LogMarker.ERROR, "Unhandled: {}", e.message, e)
//        log.error(LogMarker.ERROR, "Unhandled exception: {}", e.message, e)
        return buildProblemDetail(ErrorCode.INTERNAL_ERROR, e.message)
    }

    private fun buildProblemDetail(
        errorCode: ErrorCode,
        detail: String?
    ): ProblemDetail {
        val problemDetail = ProblemDetail.forStatus(errorCode.status)
        problemDetail.type = URI.create("${appProperties.domain}/${errorCode.name.lowercase().replace("_", "-")}")
        problemDetail.title = errorCode.title
        problemDetail.detail = detail ?: errorCode.detail
        problemDetail.setProperty("code", errorCode.toCode(appProperties.name))
        problemDetail.setProperty("timestamp", Instant.now().toString())
        return problemDetail
    }
}
