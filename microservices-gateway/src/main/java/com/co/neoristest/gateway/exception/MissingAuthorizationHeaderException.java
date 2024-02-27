package com.co.neoristest.gateway.exception;

import com.co.neoristest.common.exception.DomainException;

public class MissingAuthorizationHeaderException extends DomainException {
    public MissingAuthorizationHeaderException(String message) {
        super(message);
    }

    public MissingAuthorizationHeaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
