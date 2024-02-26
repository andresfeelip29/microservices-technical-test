package com.co.neoristest.common.exception;

import com.co.neoristest.common.exception.DomainException;

public class AccountHasNoBalanceException extends DomainException {

    public AccountHasNoBalanceException(String message) {
        super(message);
    }

    public AccountHasNoBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
