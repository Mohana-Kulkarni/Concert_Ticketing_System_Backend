package com.example.concertsystem.exception_handling.classes;

public class VenueNotFoundException extends RuntimeException{
    public VenueNotFoundException(String message) {
        super(message);
    }

    public VenueNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public VenueNotFoundException(Throwable cause) {
        super(cause);
    }
}
