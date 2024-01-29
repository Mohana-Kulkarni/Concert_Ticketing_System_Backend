package com.example.concertsystem.entity;

import java.io.Serializable;

public record Organiser(
        String id,
        String name,
        String userName,
        String email,
        String govId,
        String walletId,
        String transactionId
) implements Serializable {
}
