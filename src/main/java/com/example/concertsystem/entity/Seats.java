package com.example.concertsystem.entity;

import com.faunadb.client.types.Value;

public record Seats(
        String id,
        boolean is_booked,
        Value.RefV tier_id,
        Value.RefV event_id
) {
}
