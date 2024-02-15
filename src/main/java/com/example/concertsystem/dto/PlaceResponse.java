package com.example.concertsystem.dto;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

public record PlaceResponse(
        String id,
        @NotEmpty(message = "City cannot be null or empty")
        String city,
        boolean popular

) implements Serializable {
}
