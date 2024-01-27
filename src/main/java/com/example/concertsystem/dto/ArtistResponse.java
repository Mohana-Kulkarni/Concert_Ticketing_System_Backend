package com.example.concertsystem.dto;

import java.io.Serializable;

public record ArtistResponse(
        String id,
        String name,
        String userName,
        String email,
        String govId,
        String profileImg
) implements Serializable  {
}
