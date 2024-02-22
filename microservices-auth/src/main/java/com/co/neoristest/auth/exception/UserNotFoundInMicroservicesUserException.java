package com.co.neoristest.auth.exception;

import com.co.neoristest.common.exception.DomainException;

public class UserNotFoundInMicroservicesUserException extends DomainException {

    public UserNotFoundInMicroservicesUserException(String message) {
        super(message);
    }

    public UserNotFoundInMicroservicesUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
