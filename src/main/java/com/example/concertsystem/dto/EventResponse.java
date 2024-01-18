package com.example.concertsystem.dto;

import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.entity.User;

import java.util.List;

public record EventResponse (
        String id,
        String name,
        String dateAndTime,
        String description,
        String eventDuration,
        String venueId,
        List<User> userId,
        List<Tier> tierId

){
}
