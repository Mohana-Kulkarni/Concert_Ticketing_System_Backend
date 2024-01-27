package com.example.concertsystem.entity;

import java.io.Serializable;

public record Artist(
        String id,
        String name,
        String userName,
        String email,
        String govId
) implements Serializable {
}
