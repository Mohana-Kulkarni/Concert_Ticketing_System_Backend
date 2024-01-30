package com.example.concertsystem.entity;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public record Event(
        String id,
        String name,
        String dateAndTime,
        String description,
        String eventDuration,
        List<String> categoryList,
        String venueId,
        List<Artist> artistList,
        List<Tier> tierList
) implements Serializable {
}
