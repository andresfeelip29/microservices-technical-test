package com.co.neoristest.auth.services;

import com.co.neoristest.auth.domain.dto.TokenResponse;
import com.co.neoristest.auth.domain.dto.UserLoginDto;
import com.co.neoristest.auth.domain.dto.UserRequestDto;
import com.co.neoristest.auth.domain.dto.UserResponseDto;


public interface AuthService {

    UserResponseDto saveUserToMicroservicesUsers(UserRequestDto userRequestDto);

    TokenResponse userAuthenticate(UserLoginDto userLoginDto);

    String validateToken(String token);

}
