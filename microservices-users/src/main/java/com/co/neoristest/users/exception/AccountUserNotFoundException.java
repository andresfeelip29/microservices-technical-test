package com.co.neoristest.users.exception;

import com.co.neoristest.common.exception.DomainException;

public class AccountUserNotFoundException extends DomainException {
    public AccountUserNotFoundException(String message) {
        super(message);
    }

    public AccountUserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
