package com.co.neoristest.accounts.exception;

import com.co.neoristest.common.exception.DomainException;

public class BalanceNegativeException extends DomainException {

    public BalanceNegativeException(String message) {
        super(message);
    }

    public BalanceNegativeException(String message, Throwable cause) {
        super(message, cause);
    }
}
