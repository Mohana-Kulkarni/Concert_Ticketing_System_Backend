package com.example.concertsystem.dto;

import com.example.concertsystem.entity.Event;
import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.entity.User;

public record TicketResponse(
        String id,
        int count,
        float cost,
        User user,
        Tier tier,
        Event eventId
) {
}
