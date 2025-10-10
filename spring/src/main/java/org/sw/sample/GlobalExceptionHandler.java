package org.sw.sample;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.sw.sample.exception.InternalServerError;
import org.sw.sample.exception.NotFoundException;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handlerNotFoundExceptions(HttpServletRequest request,
                                                                   NotFoundException cause) {
        try {
            ProblemDetail problem = ProblemDetail.forStatus(cause.getStatus().value());
            problem.setType(URI.create(request.getRequestURI()));
            problem.setTitle(cause.getReason());
            problem.setDetail(cause.getMessage());
            problem.setInstance(null);  // api-documents
            return ResponseEntity
                    .status(problem.getStatus())
                    .header("Content-Type", "application/problem+json")
                    .body(problem);
        } finally {
            print(cause);
        }
    }


    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<ProblemDetail> handleInternalServerErrors(HttpServletRequest servlet, InternalServerError cause) {
        try {
            ProblemDetail problem = ProblemDetail.forStatus(500);
            problem.setType(URI.create(servlet.getRequestURI())); // Request Path
            problem.setTitle(cause.getReason());
            problem.setDetail(cause.getMessage());
            problem.setInstance(null);  // api-documents
            problem.setProperties(null); // 3rd-sys messages
            return ResponseEntity
                    .status(problem.getStatus())
                    .header("Content-Type", "application/problem+json")
                    .body(problem);
        } finally {
            print(cause);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGlobalExceptions(HttpServletRequest servlet, Exception cause) {
        try {
            ProblemDetail problem = ProblemDetail.forStatus(500);
            problem.setType(URI.create(servlet.getRequestURI())); // Request Path
            problem.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

            return ResponseEntity
                    .status(problem.getStatus())
                    .header("Content-Type", "application/problem+json")
                    .body(problem);
        } finally {
            print(cause);
        }
    }

    private void print(Exception exception) {
        logger.info("Exception Occurred: {} - {}", exception.getClass().getSimpleName(), exception.getMessage());
        logger.error("Exception Occurred", exception);
    }

}
