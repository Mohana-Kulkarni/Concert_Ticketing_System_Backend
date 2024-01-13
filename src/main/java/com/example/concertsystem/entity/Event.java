package com.example.concertsystem.entity;

import com.faunadb.client.types.Value;

import java.time.LocalDateTime;
import java.util.List;

public record Event(
        String id,
        String name,
        String dateAndTime,
        String description,
        String venueId,
        List<String> userId,
        List<String> tierId
) {
}
