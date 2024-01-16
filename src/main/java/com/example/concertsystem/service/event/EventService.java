package com.example.concertsystem.service.event;

import com.example.concertsystem.entity.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface EventService {

    void addEvent(String name, String date, String description, String eventDuration, String venueId, List<String> userId, List<String> tierId) throws ExecutionException, InterruptedException;
    void updateEvent(String id, String name, String date, String description,  String eventDuration, String venueName, List<String> userId, List<String> tierId) throws ExecutionException, InterruptedException;
    List<Event> getEventByArtist(String artist) throws ExecutionException, InterruptedException;
    List<Event> getEventByPlace(String place) throws ExecutionException, InterruptedException;
    Event getEventById(String id) throws ExecutionException, InterruptedException;
    List<Event> getEventByVenue(String venue) throws ExecutionException, InterruptedException;
    String getEventIdByName(String eventName) throws ExecutionException, InterruptedException;

    List<Event> getAllEvents() throws ExecutionException, InterruptedException;
    void deleteEventById(String id);


}
