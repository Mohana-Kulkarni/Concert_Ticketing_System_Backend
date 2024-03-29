package com.example.concertsystem.dto;

import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.entity.User;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public record TicketResponse(
        String id,
        int count,
        String cost,
        UserResponse user,
        String vcId,
        Tier tier,
        EventResponse eventId,
        String transactionId,
        Map<String, Map<String, String >> nfts
) implements Serializable {
}
