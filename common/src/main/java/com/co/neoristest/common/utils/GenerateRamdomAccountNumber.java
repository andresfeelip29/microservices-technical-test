package com.co.neoristest.common.utils;

import java.util.concurrent.ThreadLocalRandom;

public class GenerateRamdomAccountNumber {

    private GenerateRamdomAccountNumber() {
    }

    public static String generateBankAccountNumber() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return String.valueOf(random.nextLong(10_000_000_000L, 100_000_000_000L));
    }
}
