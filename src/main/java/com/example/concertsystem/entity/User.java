package com.example.concertsystem.entity;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.sql.Statement;

public record User(
        String id,
        String name,
        String userName,
        String walletId,
        String userEmail,
        MultipartFile profileImg
) implements Serializable {
}
