package com.co.neoristest.transactions.exception;

import com.co.neoristest.common.exception.DomainException;

public class BalanceUpdateException extends DomainException {

    public BalanceUpdateException(String message) {
        super(message);
    }

    public BalanceUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
