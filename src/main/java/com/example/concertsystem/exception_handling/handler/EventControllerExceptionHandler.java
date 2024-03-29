package com.example.concertsystem.exception_handling.handler;

import com.example.concertsystem.exception_handling.classes.EventNotFoundException;
import com.example.concertsystem.exception_handling.response.EventErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EventControllerExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<EventErrorResponse> handleException(EventNotFoundException exception) {
        EventErrorResponse response = new EventErrorResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(exception.getMessage());
        response.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<EventErrorResponse> handleExceptions(Exception e) {
        EventErrorResponse response = new EventErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(e.getMessage());
        response.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
