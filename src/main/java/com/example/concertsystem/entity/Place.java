package com.example.concertsystem.entity;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

public record Place(
        String id,
        @NotEmpty(message = "City cannot be null or empty")
        String city
) implements Serializable {
}
