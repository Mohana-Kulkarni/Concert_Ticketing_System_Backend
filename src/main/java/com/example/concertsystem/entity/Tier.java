package com.example.concertsystem.entity;

import com.faunadb.client.types.Value;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record Tier(
        String id,
        @NotEmpty(message = "Name cannot be null or empty")
        String name,
        @NotNull(message = "Capacity cannot be null or empty")
        int capacity,
        @NotNull(message = "Price cannot be null or empty")
        int price


) implements Serializable {
}
