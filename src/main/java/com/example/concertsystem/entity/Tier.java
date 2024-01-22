package com.example.concertsystem.entity;

import com.faunadb.client.types.Value;

import java.io.Serializable;

public record Tier(
        String id,
        String name,
        int capacity,
        int price


) implements Serializable {
}
