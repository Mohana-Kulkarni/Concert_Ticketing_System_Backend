package com.example.concertsystem.entity;

import com.faunadb.client.types.Value;

import java.time.LocalDateTime;
import java.util.List;

public record Event(
        String id,
        String name,
        LocalDateTime dateAndTime,
        String description,
        Value.RefV venue_id,
        List<Value.RefV> userId,
        List<Value.RefV> tierId
) {
}
