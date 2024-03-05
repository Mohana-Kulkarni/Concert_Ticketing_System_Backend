package com.example.concertsystem.dto;

import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.entity.User;

import java.io.Serializable;
import java.util.List;

public record TicketResponse(
        String id,
        int count,
        float cost,
        UserResponse user,
        Tier tier,
        EventResponse eventId,
        List<String> nfts
) implements Serializable {
}
