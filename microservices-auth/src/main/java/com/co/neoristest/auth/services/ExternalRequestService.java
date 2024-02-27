package com.co.neoristest.auth.services;

import com.co.neoristest.auth.domain.dto.UserRequestDto;
import com.co.neoristest.auth.domain.dto.UserResponseDto;

public interface ExternalRequestService {

     UserRequestDto findUserInMicroservicesUserByUsername(String username);

     UserResponseDto saveUserToMicroservicesUsers(UserRequestDto userRequestDto);
}
