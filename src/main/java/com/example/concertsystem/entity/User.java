package com.example.concertsystem.entity;

import java.io.Serializable;

public record User(
        String id,
        String name,
        String typeOfUser,
        String userName,
        String profileImg
) implements Serializable {
}
