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
        List<MultipartFile> images,
        String venueId,
        List<String> userId,
        List<Tier> tierId
) implements Serializable {
}
