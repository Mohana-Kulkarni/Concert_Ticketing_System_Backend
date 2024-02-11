package com.example.concertsystem.entity;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.sql.Statement;

public record User(
        String id,
        @NotEmpty(message = "Name cannot be null or empty")
        String name,
        @NotEmpty(message = "userName cannot be null or empty")
        String userName,
        @NotEmpty(message = "userEmail cannot be null or empty")
        String userEmail,
        @NotEmpty(message = "walletId cannot be null or empty")
        String walletId,
        @NotEmpty(message = "transactionId cannot be null or empty")
        String transactionId,
        @NotEmpty(message = "profileImg cannot be null or empty")
        String profileImg
) implements Serializable {
}
