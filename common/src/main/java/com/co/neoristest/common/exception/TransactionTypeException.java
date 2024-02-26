package com.co.neoristest.common.exception;

public class TransactionTypeException extends DomainException{

    public TransactionTypeException(String message) {
        super(message);
    }

    public TransactionTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
