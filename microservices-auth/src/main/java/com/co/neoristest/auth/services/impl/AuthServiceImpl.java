package com.co.neoristest.auth.services.impl;

import com.co.neoristest.auth.domain.dto.TokenResponse;
import com.co.neoristest.auth.domain.dto.UserLoginDto;
import com.co.neoristest.auth.domain.dto.UserRequestDto;
import com.co.neoristest.auth.domain.dto.UserResponseDto;
import com.co.neoristest.auth.security.JwtGenerate;
import com.co.neoristest.auth.services.AuthService;
import com.co.neoristest.auth.services.ExternalRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final ExternalRequestService externalRequestService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtGenerate jwtGenerate;


    public AuthServiceImpl(ExternalRequestService externalRequestService,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtGenerate jwtGenerate) {
        this.externalRequestService = externalRequestService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtGenerate = jwtGenerate;
    }

    @Override
    public UserResponseDto saveUserToMicroservicesUsers(UserRequestDto userRequestDto) {
        if (Objects.isNull(userRequestDto)) return null;
        log.info("Se procede a enviar dede microservicio auth a usuario con username: {}", userRequestDto.username());
        return this.externalRequestService.saveUserToMicroservicesUsers(new UserRequestDto(userRequestDto.username(),
                this.passwordEncoder.encode(userRequestDto.password()), userRequestDto.status()));
    }

    @Override
    public  TokenResponse userAuthenticate(UserLoginDto userLoginDto) {
        TokenResponse response = null;
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.username(),
                userLoginDto.password()));
        if (authenticate.isAuthenticated()) {
            response = new TokenResponse(this.jwtGenerate.generateToken(userLoginDto.username()));
        }
        return response;
    }

    @Override
    public String validateToken(String token) {
        this.jwtGenerate.validateToken(token);
        return "Token is valid";
    }
}
