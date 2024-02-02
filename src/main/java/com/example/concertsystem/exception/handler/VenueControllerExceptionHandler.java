package com.example.concertsystem.exception.handler;

import com.example.concertsystem.exception.classes.VenueNotFoundException;
import com.example.concertsystem.exception.response.VenueErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class VenueControllerExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<VenueErrorResponse> handleException(VenueNotFoundException exception) {
        VenueErrorResponse response = new VenueErrorResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(exception.getMessage());
        response.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<VenueErrorResponse> handleExceptions(Exception e) {
        VenueErrorResponse response = new VenueErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(e.getMessage());
        response.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
