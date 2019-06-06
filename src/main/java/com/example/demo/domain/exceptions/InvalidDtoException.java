package com.example.demo.domain.exceptions;

public class InvalidDtoException extends RuntimeException {
  public InvalidDtoException(String message) {
    super(message);
  }
}
