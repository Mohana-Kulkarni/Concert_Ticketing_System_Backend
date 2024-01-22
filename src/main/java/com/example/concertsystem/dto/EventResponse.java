package com.example.concertsystem.dto;

import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.entity.User;

import java.io.Serializable;
import java.util.List;

public record EventResponse (
        String id,
        String name,
        String dateAndTime,
        String description,
        String eventDuration,
        List<String> imageUrls,
        String venueId,
        List<User> userId,
        List<Tier> tierId

) implements Serializable {
    public String getId() {
        return id;
    }
}
