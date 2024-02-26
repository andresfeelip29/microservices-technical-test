package com.co.neoristest.transactions.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionDto(@Positive @NotNull BigDecimal transactionValue,
                             @NotBlank String accountOriginNumber,
                             @NotBlank String accountDestinyNumber) {
}
