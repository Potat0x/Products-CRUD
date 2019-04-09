package com.example.demo.infrastructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class EmptyProductNameException extends RuntimeException {
    public EmptyProductNameException(String message) {
        super(message);
    }
}