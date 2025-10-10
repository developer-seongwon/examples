package org.sw.sample.exception;

import org.springframework.http.HttpStatus;

public class InternalServerError extends CommonException {


    private InternalServerError(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    private InternalServerError(String message, Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message, cause);
    }

    @Override
    public String getReason() {
        return HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
    }

    /**
     * 메시지로 InternalServerError 생성
     *
     * @param message 커스텀 에러 메시지
     * @return InternalServerError 인스턴스
     */
    public static InternalServerError of(String message) {
        return new InternalServerError(message);
    }

    /**
     * 원인 예외와 커스텀 메시지로 InternalServerError 생성
     *
     * @param message 커스텀 에러 메시지
     * @param cause   원인 예외
     * @return InternalServerError 인스턴스
     */
    public static InternalServerError of(String message, Throwable cause) {
        return new InternalServerError(message, cause);
    }
}
