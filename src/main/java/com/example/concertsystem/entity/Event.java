package com.example.concertsystem.entity;

import java.util.List;

public record Event(
        String id,
        String name,
        String dateAndTime,
        String description,
        String eventDuration,
        String venueId,
        List<String> userId,
        List<String> tierId
) {
}
