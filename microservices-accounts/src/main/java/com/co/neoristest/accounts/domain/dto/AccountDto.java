package com.co.neoristest.accounts.domain.dto;

import com.co.neoristest.common.domain.enums.BankAccountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AccountDto(@NotNull BankAccountType accountType, @NotNull @Positive BigDecimal balance,
                         @NotNull Boolean status, @Positive Long userId) { }
