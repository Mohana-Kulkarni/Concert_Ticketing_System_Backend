package com.example.concertsystem.service.event;

import com.example.concertsystem.entity.Event;

public interface EventService {

    void addEvent(Event event);
    Event updateEvent(String id, Event event);
    Event getEventByArtist(String artist);
    Event getEventByPlace(String place);
    Event getEventById(String id);
    void deleteEventById(String id);

}
