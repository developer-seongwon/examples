package org.sw.member.domain.exception

/**
 * 도메인 예외 베이스 클래스
 */
abstract class CommonException(
    val errorCode: ErrorCode,
    override val message: String = errorCode.detail
) : RuntimeException(message)

/**
 * HTTP 상태 기준 공통 예외
 * ErrorCode로 세부 분류한다.
 */
open class BadRequest(errorCode: ErrorCode, message: String = errorCode.detail) :
    CommonException(errorCode, message)

open class NotFound(errorCode: ErrorCode, message: String = errorCode.detail) :
    CommonException(errorCode, message)

open class Conflict(errorCode: ErrorCode, message: String = errorCode.detail) :
    CommonException(errorCode, message)

open class InternalServerError(errorCode: ErrorCode = ErrorCode.INTERNAL_ERROR, message: String = errorCode.detail) :
    CommonException(errorCode, message)
