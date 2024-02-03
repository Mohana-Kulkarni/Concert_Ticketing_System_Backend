package com.example.concertsystem.entity;

import com.faunadb.client.types.Value;

import java.io.Serializable;
import java.util.List;

public record Ticket(
        String id,
        int count,
        float cost,
        String transactionId,
        String userId,
        String tierId,
        String eventId,
        String nftToken
) implements Serializable  {
}
