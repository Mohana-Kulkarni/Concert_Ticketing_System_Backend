package com.example.concertsystem.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

public record Organiser(
        String id,
        @NotEmpty(message = "Name cannot be null or empty")
        String name,
        @NotEmpty(message = "User Name cannot be null or empty")
        String userName,
        @NotEmpty(message = "Email cannot be null or empty")
        @Email(message = "Email address should be a valid value")
        String email,
        @NotEmpty(message = "govId cannot be null or empty")
        String govId,
        @NotEmpty(message = "walletId cannot be null or empty")
        String walletId,
        @NotEmpty(message = "transactionId cannot be null or empty")
        String transactionId
) implements Serializable {
}
