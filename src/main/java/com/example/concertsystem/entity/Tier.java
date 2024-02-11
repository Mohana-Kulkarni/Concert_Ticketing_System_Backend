package com.example.concertsystem.entity;

import com.faunadb.client.types.Value;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record Tier(
        String id,
        @NotEmpty(message = "Name cannot be null or empty")
        String name,
        @NotNull(message = "Capacity cannot be null or empty")
        @Min(value = 1, message = "Value must be greater than or equal to 1")
        int capacity,
        @NotNull(message = "Price cannot be null or empty")
        @Min(value = 0, message = "Value must be greater than or equal to 1")
        int price


) implements Serializable {
}
