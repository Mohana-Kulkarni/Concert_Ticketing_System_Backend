package com.example.concertsystem.exception.classes;

public class EventNotFoundException extends RuntimeException{

    public EventNotFoundException(String message) {
        super(message);
    }

    public EventNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventNotFoundException(Throwable cause) {
        super(cause);
    }
}
