package com.co.neoristest.accounts.exception;

import com.co.neoristest.common.exception.DomainException;

public class ClientNotFoundException extends DomainException {

    public ClientNotFoundException(String message) {
        super(message);
    }

    public ClientNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
