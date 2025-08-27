package com.example.bbapp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class BarberAPIException extends RuntimeException{

    private HttpStatus status;
    private String message;
}
