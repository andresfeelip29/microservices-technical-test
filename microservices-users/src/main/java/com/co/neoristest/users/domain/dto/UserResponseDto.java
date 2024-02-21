package com.co.neoristest.users.domain.dto;

import com.co.neoristest.users.domain.AccountDetail;


import java.util.List;

public record UserResponseDto(Long id, String username, Boolean status,
                              List<Long> accounts,
                              List<AccountDetail> accountDetails) {
}
