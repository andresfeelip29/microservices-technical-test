package com.co.neoristest.accounts.exception;

import com.co.neoristest.common.exception.DomainException;

public class ClientAccountNotFoundException extends DomainException {

    public ClientAccountNotFoundException(String message) {
        super(message);
    }

    public ClientAccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
