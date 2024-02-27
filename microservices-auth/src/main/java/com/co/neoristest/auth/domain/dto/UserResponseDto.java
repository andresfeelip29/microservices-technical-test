package com.co.neoristest.auth.domain.dto;

import java.util.List;

public record UserResponseDto(Long id, String username, Boolean status,
                              List<Long> accounts) {
}
