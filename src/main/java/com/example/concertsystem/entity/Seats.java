package com.example.concertsystem.entity;

import com.faunadb.client.types.Value;

import java.io.Serializable;

public record Seats(
        String id,
        boolean isBooked,
        String tierId,
        String eventId
) implements Serializable {
}
