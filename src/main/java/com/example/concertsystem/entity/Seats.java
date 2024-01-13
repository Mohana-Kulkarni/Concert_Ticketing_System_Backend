package com.example.concertsystem.entity;

import com.faunadb.client.types.Value;

public record Seats(
        String id,
        boolean isBooked,
        String tierId,
        String eventId
) {
}
