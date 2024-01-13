package com.example.concertsystem.entity;

import com.faunadb.client.types.Value;

public record Tier(
        String id,
        String name,
        int capacity,
        int price

) {
}
