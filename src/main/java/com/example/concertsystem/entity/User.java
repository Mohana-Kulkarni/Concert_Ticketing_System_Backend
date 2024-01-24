package com.example.concertsystem.entity;

import java.io.Serializable;
import java.sql.Statement;

public record User(
        String id,
        String name,
        String userName,
        String walletId,
        String userEmail,
        String profileImg
) implements Serializable {
}
