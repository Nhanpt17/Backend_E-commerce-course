package com.human.graduateproject.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {

        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler( IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException( IOException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler( ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException( ExpiredJwtException ex) {
        return new ResponseEntity<>(new ErrorResponse("Token hết hạn!"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler( JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException ( JwtException ex) {
        return new ResponseEntity<>(new ErrorResponse("Token không hợp lệ!"), HttpStatus.BAD_REQUEST);
    }
}
