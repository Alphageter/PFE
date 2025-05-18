package com.gestionpfes.adnan.handleErrores.handleErrortest;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}