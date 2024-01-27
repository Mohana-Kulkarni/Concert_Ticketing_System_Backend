package com.example.concertsystem.dto;

public record ArtistResponse(
        String id,
        String name,
        String userName,
        String email,
        String govId,
        String profileImg
) {
}
