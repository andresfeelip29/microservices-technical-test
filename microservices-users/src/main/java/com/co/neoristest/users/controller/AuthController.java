package com.co.neoristest.users.controller;

import com.co.neoristest.users.domain.dto.UserLoginResponseDto;
import com.co.neoristest.users.domain.dto.UserResponseDto;
import com.co.neoristest.users.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/usuarios/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/login")
    public ResponseEntity<UserLoginResponseDto> loginUserByUsername(@RequestParam(name = "username") String username) {
        log.info("Se recibe peticion de consulta de usuario para logueo con nombre de usuario: {}", username);
        return this.userService.findUserByUsername(username)
                .map(user -> ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(user))
                .orElse(ResponseEntity.badRequest().build());
    }


    @GetMapping("/authorized")
    public Map<String, String> authorized(@RequestParam(name = "code") String code) {
        log.info("Se recibe peticion de consulta de autorizacion");
        return Collections.singletonMap("code", code);
    }


}
