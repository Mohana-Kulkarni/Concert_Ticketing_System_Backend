package com.example.concertsystem.entity;

import com.faunadb.client.types.Value;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record Venue(
        String id,
        @NotEmpty(message = "Name cannot be null or empty")
        String name,
        @NotEmpty(message = "address cannot be null or empty")
        String address,
        @NotNull(message = "capacity cannot be null or empty")
        @Min(value = 0, message = "Value must be greater than or equal to 0")
        int capacity,
        @NotEmpty(message = "placeId cannot be null or empty")
        String placeId
) implements Serializable  {
}
