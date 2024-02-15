package com.example.concertsystem.entity;

import com.faunadb.client.types.Value;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

public record Ticket(
        String id,
        @NotNull(message = "Count cannot be null or empty")
        @Min(value = 1, message = "Value must be greater than or equal to 1")
        int count,
        @NotNull(message = "Cost cannot be null or empty")
        @DecimalMin(value = "0.0",inclusive = true, message = "Value must be greater than or equal to 0.0")
        float cost,
        @NotEmpty(message = "transactionId cannot be null or empty")
        String transactionId,
        @NotEmpty(message = "userId cannot be null or empty")
        String userId,
        @NotEmpty(message = "tierId cannot be null or empty")
        String tierId,
        @NotEmpty(message = "eventId cannot be null or empty")
        String eventId,
        @NotEmpty(message = "nftToken List cannot be null or empty")
        List<String> nftToken
) implements Serializable  {
}
