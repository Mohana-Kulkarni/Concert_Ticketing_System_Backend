package com.example.concertsystem.entity;

public record User(
        String id,
        String name,
        String typeOfUser,
        String userName,
        String profileImg
) {
}
