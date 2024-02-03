package com.example.concertsystem.exception_handling.classes;

public class OrganiserNotFoundException extends RuntimeException{
    public OrganiserNotFoundException(String message) {
        super(message);
    }

    public OrganiserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrganiserNotFoundException(Throwable cause) {
        super(cause);
    }
}
