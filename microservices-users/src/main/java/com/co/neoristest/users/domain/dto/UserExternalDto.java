package com.co.neoristest.users.domain.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public record UserExternalDto(Long id, String username, Boolean status) {
}
