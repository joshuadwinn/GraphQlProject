package com.springgraphql.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ApiException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final String errorCode;
    public ApiException(HttpStatus httpStatus, String errorCode, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public ApiException(HttpStatus httpStatus, String errorCode) {
        this(httpStatus, errorCode, "");
    }

    public ApiException(HttpStatusCode httpStatusCode, String errorCode, String message) {
        this(HttpStatus.valueOf(httpStatusCode.value()), errorCode, message);
    }

    public ApiException(HttpStatusCode httpStatusCode, String errorCode) {
        this(httpStatusCode, errorCode, "");
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
