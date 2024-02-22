package com.co.neoristest.accounts.exception;

import com.co.neoristest.common.exception.DomainException;

public class AccountNotFoundException extends DomainException {
    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
