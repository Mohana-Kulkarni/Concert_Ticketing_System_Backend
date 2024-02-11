package com.example.concertsystem.entity;

import com.faunadb.client.types.Value;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

public record Tier(
        String id,
        @NotEmpty(message = "Name cannot be null or empty")
        String name,
        @NotEmpty(message = "Capacity cannot be null or empty")
        int capacity,
        @NotEmpty(message = "Price cannot be null or empty")
        int price


) implements Serializable {
}
