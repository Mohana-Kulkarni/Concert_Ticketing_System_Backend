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
        String venueId,
        List<MultipartFile> images,
        List<Artist> artistList,
        List<MultipartFile> profileImages,
        List<Tier> tierList
) implements Serializable {
}
