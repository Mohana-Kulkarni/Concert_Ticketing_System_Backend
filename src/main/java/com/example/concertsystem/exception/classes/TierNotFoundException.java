package com.example.concertsystem.exception.classes;

public class TierNotFoundException extends RuntimeException{
    public TierNotFoundException(String message) {
        super(message);
    }

    public TierNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TierNotFoundException(Throwable cause) {
        super(cause);
    }
}
