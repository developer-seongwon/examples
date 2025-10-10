package org.sw.sample.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CommonException {
    public static final String NOT_FOUND_PATH = "Not Found Path";
    private final String reason;

    private NotFoundException(String reason, String message) {
        super(HttpStatus.NOT_FOUND, message);
        this.reason = reason;
    }

    @Override
    public String getReason() {
        return this.reason;
    }

    /**
     * 요청한 데이터를 찾을 수 없을 때 사용
     *
     * @param message 에러 메시지
     * @return exception
     */
    public static NotFoundException ofPath(String message) {
        return new NotFoundException(NOT_FOUND_PATH, "요청하신 경로를 찾을 수 없습니다: " + message);
    }


}
