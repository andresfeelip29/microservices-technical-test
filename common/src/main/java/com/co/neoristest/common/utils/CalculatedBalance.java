package com.co.neoristest.common.utils;

import java.math.BigDecimal;

public class CalculatedBalance {

    private CalculatedBalance() {
    }

    public static Boolean isLessThanZero(BigDecimal mount) {
        return mount.compareTo(BigDecimal.ZERO) < 0;
    }

    public static Boolean isZero(BigDecimal mount) {
        return mount.compareTo(BigDecimal.ZERO) == 0;
    }
    public static BigDecimal addAmount(BigDecimal value, BigDecimal balance) {
        return balance.add(value);
    }

    public static BigDecimal subtractAmount(BigDecimal value, BigDecimal balance) {
        return balance.subtract(value);
    }

    public static Boolean hasBalanceForTransaction(BigDecimal balance, BigDecimal transactionValue) {
        return isLessThanZero(subtractAmount(transactionValue, balance));
    }

}
