package com.co.neoristest.auth.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record UserLoginDto(@NotNull @NotBlank String username,
                           @NotNull @NotBlank String password) {
}
