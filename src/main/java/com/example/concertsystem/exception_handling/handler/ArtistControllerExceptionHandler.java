package com.example.concertsystem.exception_handling.handler;


import com.example.concertsystem.exception_handling.classes.ArtistNotFoundException;
import com.example.concertsystem.exception_handling.response.ArtistErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ArtistControllerExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ArtistErrorResponse> handleException(ArtistNotFoundException exception) {
        ArtistErrorResponse response = new ArtistErrorResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(exception.getMessage());
        response.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ArtistErrorResponse> handleExceptions(Exception e) {
        ArtistErrorResponse response = new ArtistErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(e.getMessage());
        response.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
