package com.example.concertsystem.exception.handler;

import com.example.concertsystem.exception.classes.PlaceNotFoundException;
import com.example.concertsystem.exception.classes.UserNotFoundException;
import com.example.concertsystem.exception.response.ArtistErrorResponse;
import com.example.concertsystem.exception.response.PlaceErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PlaceControllerExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<PlaceErrorResponse> handleException(PlaceNotFoundException exception) {
        PlaceErrorResponse response = new PlaceErrorResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(exception.getMessage());
        response.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PlaceErrorResponse> handleExceptions(Exception e) {
        PlaceErrorResponse response = new PlaceErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(e.getMessage());
        response.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
