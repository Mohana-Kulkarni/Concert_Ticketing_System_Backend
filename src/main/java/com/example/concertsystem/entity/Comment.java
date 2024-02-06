package com.example.concertsystem.entity;

import java.io.Serializable;

public record Comment(
        String id,
        String content,
        String userId,
        String eventId,
        String timestamp
) implements Serializable {
}
