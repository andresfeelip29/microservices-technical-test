package com.co.neoristest.common.utils;

import java.math.BigDecimal;

public class CalculatedBalance {

    private  CalculatedBalance() {
    }

    public static Boolean isLessThanZero(BigDecimal mount) {
        return mount.compareTo(BigDecimal.ZERO) < 0;
    }

    public static BigDecimal calculateBalance(BigDecimal value, BigDecimal initialBalance) {
        return initialBalance.add(value);
    }
}
