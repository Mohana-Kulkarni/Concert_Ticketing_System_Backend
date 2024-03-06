package com.example.concertsystem.dto;

import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.entity.User;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public record TicketResponse(
        String id,
        int count,
        float cost,
        UserResponse user,
        Tier tier,
        EventResponse eventId,
        Map<String, Boolean> nfts
) implements Serializable {
}
