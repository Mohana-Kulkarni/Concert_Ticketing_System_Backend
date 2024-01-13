package com.example.concertsystem.entity;

import com.faunadb.client.types.Value;

import java.util.List;

public record Ticket(
        String id,
        int count,
        String userId,
        String tierId,
        String eventId
) {
}
