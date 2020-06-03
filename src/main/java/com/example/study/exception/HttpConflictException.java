package com.example.study.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class HttpConflictException extends HttpStatusCodeException {

    public HttpConflictException(String statusText) {
        super(HttpStatus.CONFLICT, statusText);
    }
}
