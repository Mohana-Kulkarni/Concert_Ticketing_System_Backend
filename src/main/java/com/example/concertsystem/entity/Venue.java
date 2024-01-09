package com.example.concertsystem.entity;

import com.faunadb.client.types.Value;

public record Venue(
        String id,
        String name,
        String address,
        int capacity,
        Value.RefV place_id,
        Value.RefV event_id
) {
}
