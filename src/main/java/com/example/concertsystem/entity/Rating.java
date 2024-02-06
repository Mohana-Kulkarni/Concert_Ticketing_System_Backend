package com.example.concertsystem.entity;

import java.io.Serializable;

public record Rating(
        String id,
        double rating,
        String userId,
        String artistId,
        String timestamp
) implements Serializable {
}
