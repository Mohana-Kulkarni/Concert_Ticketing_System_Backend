package com.example.concertsystem.entity;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public record Event(
        String id,
        @NotEmpty(message = "Name cannot be null or empty")
        String name,
        @NotEmpty(message = "Date cannot be null or empty")
        String dateAndTime,
        @NotEmpty(message = "Description cannot be null or empty")
        String description,
        @NotEmpty(message = "Event Duration cannot be null or empty")
        String eventDuration,
        @NotEmpty(message = "Category List cannot be null or empty")
        List<String> categoryList,
        @NotEmpty(message = "VenueId cannot be null or empty")
        String venueId,
        @NotEmpty(message = "Artist List cannot be null or empty")
        List<String> artistList,
        @NotEmpty(message = "Tier List cannot be null or empty")
        List<Tier> tierList
) implements Serializable {
}
