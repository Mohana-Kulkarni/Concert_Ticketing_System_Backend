package com.example.concertsystem.dto;

import org.springframework.web.multipart.MultipartFile;

public record UserResponse(
        String id,
        String name,
        String userName,
        String walletId,
        String userEmail,
        String profileImg
) {
}
