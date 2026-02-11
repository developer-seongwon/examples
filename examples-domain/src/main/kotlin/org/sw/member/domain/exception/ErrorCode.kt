package org.sw.member.domain.exception

/**
 * 에러 코드 정의
 *
 * code 형식: {SERVER_NAME(3)}-{HTTP_STATUS}-{SEQ(2)}
 * 예) EXP-404-01, EXP-409-01, EXP-400-01
 *
 * SERVER_NAME은 런타임에 BuildConfig에서 주입받아 조합한다.
 */
enum class ErrorCode(
    val status: Int,
    val seq: String,
    val title: String,
    val detail: String
) {
    // 400
    INVALID_PARAMETER(400, "01", "Invalid parameter", "요청 파라미터가 유효하지 않습니다."),
    INVALID_EMAIL(400, "02", "Invalid email", "이메일 형식이 올바르지 않습니다."),
    INVALID_AGE(400, "03", "Invalid age", "나이는 0~150 사이여야 합니다."),

    // 404
    MEMBER_NOT_FOUND(404, "01", "Member not found", "회원을 찾을 수 없습니다."),

    // 409
    DUPLICATE_EMAIL(409, "01", "Duplicate email", "이미 존재하는 이메일입니다."),

    // 500
    INTERNAL_ERROR(500, "01", "Internal Server Error", "서버 내부 오류가 발생했습니다.");

    /**
     * prefix를 받아 전체 code를 생성한다.
     * 예) toCode("EXP") → "EXP-404-01"
     */
    fun toCode(prefix: String): String = "$prefix-$status-$seq"
}
