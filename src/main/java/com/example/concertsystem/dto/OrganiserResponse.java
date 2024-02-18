package com.example.concertsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;



public record OrganiserResponse(
        String id,
        String name,
        String email,
        String govId,
        String walletId,
        String transactionId,
        String profileImg,
        List<String> organisedEvents
) implements Serializable {
}
