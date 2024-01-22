package com.example.concertsystem.entity;

import java.io.Serializable;

public record Place(
        String id,
        String city
) implements Serializable {
}
