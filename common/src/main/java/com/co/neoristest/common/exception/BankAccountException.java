package com.co.neoristest.common.exception;

public class BankAccountException extends DomainException {
    public BankAccountException(String message) {
        super(message);
    }

    public BankAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
