package com.example.concertsystem.entity;

import com.faunadb.client.types.Value;

public record Venue(
        String id,
        String name,
        String address,
        int capacity,
        String placeId
) {
}
