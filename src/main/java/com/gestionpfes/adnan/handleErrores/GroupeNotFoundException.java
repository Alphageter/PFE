package com.gestionpfes.adnan.handleErrores;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(HttpStatus.NOT_FOUND)
public class GroupeNotFoundException extends RuntimeException {
    public GroupeNotFoundException(String message) { 
        super(message); } 

    
}
