package com.example.concertsystem.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public record UserResponse(
        String id,
        String userEmail,
        String walletId,
        String transactionId,
        String profileImg,
        String userDetailsId
) implements Serializable {
}
