package com.co.neoristest.auth.domain.dto;

public record UserRequestDto(String username,
                             String password,
                             Boolean status) {
}
