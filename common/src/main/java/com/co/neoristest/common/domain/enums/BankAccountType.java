package com.co.neoristest.common.domain.enums;

public enum BankAccountType {

    SAVINGS_ACCOUNT("Ahorro"),
    CHECKING_ACCOUNT("Corriente");

    private final String type;

    BankAccountType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
