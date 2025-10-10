package org.sw.sample.exception;

import org.springframework.http.HttpStatus;

public abstract class CommonException extends RuntimeException {
    private final HttpStatus status;

    public abstract String getReason();

    public CommonException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public CommonException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
