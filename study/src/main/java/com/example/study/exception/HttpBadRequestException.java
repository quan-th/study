package com.example.study.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class HttpBadRequestException extends HttpStatusCodeException {

    public HttpBadRequestException(String statusText) {
        super(HttpStatus.BAD_REQUEST, statusText);
    }
}
