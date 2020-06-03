package com.example.study.exception;

public class DataIntegrationException extends RuntimeException {

    public DataIntegrationException(String statusText) {
        super(statusText);
    }
}
