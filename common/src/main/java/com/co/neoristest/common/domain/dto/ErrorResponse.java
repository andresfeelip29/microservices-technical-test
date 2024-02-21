package com.co.neoristest.common.domain.dto;

import lombok.Builder;

@Builder
public record ErrorResponse(Integer code, String message) {
}
