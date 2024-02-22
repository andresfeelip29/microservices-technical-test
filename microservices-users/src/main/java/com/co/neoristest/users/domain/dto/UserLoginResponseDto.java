package com.co.neoristest.users.domain.dto;

public record UserLoginResponseDto(String username,
                                   String password,
                                   Boolean status) {
}
