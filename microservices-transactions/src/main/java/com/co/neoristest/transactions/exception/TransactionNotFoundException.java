package com.co.neoristest.transactions.exception;

import com.co.neoristest.common.exception.DomainException;

public class TransactionNotFoundException extends DomainException {
    public TransactionNotFoundException(String message) {
        super(message);
    }

    public TransactionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
