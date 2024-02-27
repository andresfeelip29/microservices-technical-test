package com.co.neoristest.auth.controller;

import com.co.neoristest.auth.domain.dto.TokenResponse;
import com.co.neoristest.auth.domain.dto.UserLoginDto;
import com.co.neoristest.auth.domain.dto.UserRequestDto;
import com.co.neoristest.auth.domain.dto.UserResponseDto;
import com.co.neoristest.auth.services.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> userRegister(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("Se recibe peticion para almacenar usuario desde microservicio auth {}!", userRequestDto);
        UserResponseDto result = this.authService.saveUserToMicroservicesUsers(userRequestDto);
        if (!Objects.isNull(result)) {
            log.info("Se crea usuario con id: {}", result.id());
            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(result);
        }
        log.error("Error en solicitud de creacion de usuario {}!", userRequestDto);
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> userAuthenticate(@Valid @RequestBody UserLoginDto userLoginDto) {
        log.info("Se recibe peticion de authenticacion con usuario {}!", userLoginDto);
        TokenResponse result = this.authService.userAuthenticate(userLoginDto);
        if (!Objects.isNull(result)) {
            log.info("Usuario autorizado con username: {}", userLoginDto.username());
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result);
        }
        log.error("Error en solicitud de autenticacion con usuario {}!", userLoginDto);
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
        log.info("Se recibe peticion de validacion de token: {}!", token);
        String result = this.authService.validateToken(token);
        if (result.isEmpty()) {
            log.info("Token validado: {}", token);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result);
        }
        log.error("Error en validacion de token: {}!", token);
        return ResponseEntity.badRequest().build();
    }
}
