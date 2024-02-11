package com.example.concertsystem.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

public record Artist(
        String id,
        @NotEmpty(message = "Name cannot be null or empty")
        String name,
        @NotEmpty(message = "UserName cannot be null or empty")
        String userName,
        @NotEmpty(message = "Email cannot be null or empty")
        @Email(message = "Email address should be a valid value")
        String email,
        @NotEmpty(message = "govId cannot be null or empty")
        String govId
) implements Serializable {
}
