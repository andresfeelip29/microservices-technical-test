package com.co.neoristest.users.domain.dto;

import lombok.*;


public record UserExternalDto(Long id, String username, Boolean status) {
}
