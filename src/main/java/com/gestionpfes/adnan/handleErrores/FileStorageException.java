package com.gestionpfes.adnan.handleErrores;



public class FileStorageException extends RuntimeException {

    
    public FileStorageException() {
        super();
    }

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable ex) {
        super(message,ex);
    }
    
}
