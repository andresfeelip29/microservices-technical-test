package com.co.neoristest.users.controller;

import com.co.neoristest.users.domain.dto.UserDto;
import com.co.neoristest.users.domain.dto.UserExternalDto;
import com.co.neoristest.users.domain.dto.UserResponseDto;
import com.co.neoristest.users.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1/usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        log.info("Se recibe peticion para consulta de todos los usuario");
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable Long userId) {
        log.info("Se recibe peticion de consulta de usuario con id: {}", userId);
        return this.userService.findUserById(userId)
                .map(user -> ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(user))
                .orElse(ResponseEntity.badRequest().build());
    }


    @GetMapping("/detail/{userId}")
    public ResponseEntity<UserResponseDto> findUserWithAccountDetail(@PathVariable Long userId) {
        log.info("Se recibe peticion de consulta de usuario con detalle con id: {}", userId);
        return this.userService.findUserAccountDetail(userId)
                .map(user -> ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(user))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/")
    public ResponseEntity<UserResponseDto> saveUser(@Valid @RequestBody UserDto userDto) {
        log.info("Se recibe peticion para almacenar usuario {}!", userDto);
        UserResponseDto result = this.userService.save(userDto);
        if (!Objects.isNull(result)) {
            log.info("Se crea usuario con id: {}", result.id());
            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(result);
        }
        log.error("Error en solicitud de creacion de cliente {}!", userDto);
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(
            @Valid @RequestBody UserDto userDto, @PathVariable Long userId) {
        UserResponseDto result = this.userService.update(userDto, userId);
        if (!Objects.isNull(result)) {
            log.info("Usuario actualizado con exito!");
            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(result);
        }
        log.error("Error en solicitud de actualizacion de usuario!");
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        this.userService.delete(userId);
        log.info("Cliente eliminado con exito!");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).contentType(MediaType.APPLICATION_JSON).build();
    }

    @GetMapping("/external/{userId}")
    public ResponseEntity<UserExternalDto> findUserByIdFromMicroservicesAccount(@PathVariable Long userId) {
        log.info("Se recibe peticion de consulta de microservicio cuentas, para usuario con id: {}", userId);
        return this.userService.findUserByIdFromMicroservicesAccount(userId)
                .map(user -> ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(user))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/external/")
    public ResponseEntity<Void> saveClientAccountFromMicroserviceClient(@RequestParam Long accountId, @RequestParam Long userId) {
        log.info("Se recibe peticion desde microservicio cuentas, para asociar cuenta: {} , a cliente con id: {}",
                accountId, userId);
        this.userService.associateAccountWithUser(userId, accountId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @DeleteMapping("/external/")
    public ResponseEntity<Void> deleteAccountUserFromMicroserviceAccount(@RequestParam Long userId, @RequestParam Long accountId) {
        log.info("Se recibe peticion desde microservicio cuentas, para la eliminacion de cuenta con id: {}", accountId);
        this.userService.deleteAccountUserFromMicroserviceAccount(userId, accountId);
        return ResponseEntity.noContent().build();
    }

}
