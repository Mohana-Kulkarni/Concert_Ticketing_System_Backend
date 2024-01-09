package com.example.concertsystem.entity;

import com.faunadb.client.types.Value;

public record Ticket(
        String id,
        Value.RefV user_id,
        Value.RefV seat_id
) {
}
