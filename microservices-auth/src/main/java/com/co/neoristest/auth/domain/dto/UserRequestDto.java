package com.co.neoristest.auth.domain.dto;

import com.co.neoristest.common.exception.validation.anotations.ValidUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDto(@ValidUsername @NotNull @NotBlank String username,
                             @NotNull @NotBlank String password,
                             @NotNull Boolean status) {
}
