package com.co.neoristest.users.domain.dto;

import com.co.neoristest.common.exception.validation.anotations.ValidUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDto(@ValidUsername String username,
                      @NotNull @NotBlank String password,
                      @NotNull  Boolean status) {
}
