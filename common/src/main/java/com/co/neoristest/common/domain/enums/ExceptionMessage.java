package com.co.neoristest.common.domain.enums;

public enum ExceptionMessage {

    USER_NOT_FOUND("No existe usuario registrado en sistema con id: %d!"),

    USERNAME_NOT_FOUND("No existe usuario registrado en sistema con nombre de usuario: %s!"),

    INCORRECT_CREDENTIALS("Credenciales incororrestas!");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
