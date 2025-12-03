package net.herit.heviton.common

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    companion object{
        private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

//    @ExceptionHandler(CommonException::class)
//    fun handleCommonExceptions(
//        servlet: HttpServletRequest,
//        cause: CommonException
//    ): ResponseEntity<ProblemDetail> {
//        logger.error("Exception Occurred: ${cause.message}", cause)
//        return ProblemDetail.forStatus(cause.status).apply {
//            type = URI.create(servlet.requestURI)
//            title = cause.title
//            detail = cause.detail
//            instance = URI.create(cause.instance)
//        }.let {
//            ResponseEntity.status(it.status)
//                .header("Content-Type", "application/problem+json")
//                .body(it)
//        }
//    }
}