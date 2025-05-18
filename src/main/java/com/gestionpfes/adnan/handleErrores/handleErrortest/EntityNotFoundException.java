package com.gestionpfes.adnan.handleErrores.handleErrortest;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}