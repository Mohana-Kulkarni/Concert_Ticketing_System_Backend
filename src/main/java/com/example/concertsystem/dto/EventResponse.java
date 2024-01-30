package com.example.concertsystem.dto;

import com.example.concertsystem.entity.Tier;

import java.io.Serializable;
import java.util.List;

public record EventResponse (
        String id,
        String name,
        String dateAndTime,
        String description,
        String eventDuration,
        List<String> imageUrls,
        List<String> categoryList,
        String venueId,
        List<ArtistResponse> artists,
        List<Tier> tiers

) implements Serializable {
    public String getId() {
        return id;
    }
}
