package com.example.concertsystem.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public record UserResponse(
        String id,
        String name,
        String userName,
        String walletId,
        String userEmail,
        String profileImg
) implements Serializable {
}
