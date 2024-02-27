package com.co.neoristest.auth.exception;

import com.co.neoristest.common.exception.DomainException;

public class ErrorSaveUserMicroservicesUsersException extends DomainException {

    public ErrorSaveUserMicroservicesUsersException(String message) {
        super(message);
    }

    public ErrorSaveUserMicroservicesUsersException(String message, Throwable cause) {
        super(message, cause);
    }
}
