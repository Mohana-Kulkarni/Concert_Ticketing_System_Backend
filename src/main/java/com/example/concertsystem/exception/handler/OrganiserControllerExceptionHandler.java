package com.example.concertsystem.exception.handler;

import com.example.concertsystem.exception.classes.ArtistNotFoundException;
import com.example.concertsystem.exception.classes.OrganiserNotFoundException;
import com.example.concertsystem.exception.response.ArtistErrorResponse;
import com.example.concertsystem.exception.response.OrganiserErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OrganiserControllerExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<OrganiserErrorResponse> handleException(OrganiserNotFoundException exception) {
        OrganiserErrorResponse response = new OrganiserErrorResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(exception.getMessage());
        response.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<OrganiserErrorResponse> handleExceptions(Exception e) {
        OrganiserErrorResponse response = new OrganiserErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(e.getMessage());
        response.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
