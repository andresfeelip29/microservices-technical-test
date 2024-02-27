package com.co.neoristest.gateway.exception;

import com.co.neoristest.common.exception.DomainException;

public class UnAuthorizedAccessToApplicationException extends DomainException {
    public UnAuthorizedAccessToApplicationException(String message) {
        super(message);
    }

    public UnAuthorizedAccessToApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
