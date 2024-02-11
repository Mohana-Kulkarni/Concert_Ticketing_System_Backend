package com.example.concertsystem.entity;

import com.faunadb.client.types.Value;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

public record Venue(
        String id,
        @NotEmpty(message = "Name cannot be null or empty")
        String name,
        @NotEmpty(message = "address cannot be null or empty")
        String address,
        @NotEmpty(message = "capacity cannot be null or empty")
        int capacity,
        @NotEmpty(message = "placeId cannot be null or empty")
        String placeId
) implements Serializable  {
}
