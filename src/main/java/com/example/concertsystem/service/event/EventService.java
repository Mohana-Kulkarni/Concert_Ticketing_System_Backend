package com.example.concertsystem.service.event;

import com.example.concertsystem.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    void addEvent(String name, String date, String description, String venueId, List<String> userId, List<String> tierId);
    Event updateEvent(String id, Event event);
    Event getEventByArtist(String artist);
    Event getEventByPlace(String place);
    Event getEventById(String id);
    void deleteEventById(String id);
    Event getEventByVenue(String venue);


}
