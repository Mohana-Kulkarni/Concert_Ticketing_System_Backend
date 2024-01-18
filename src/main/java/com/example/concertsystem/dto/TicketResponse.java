package com.example.concertsystem.dto;

import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.entity.User;

public record TicketResponse(
        String id,
        int count,
        float cost,
        User user,
        Tier tier,
        EventResponse eventId
) {
}
