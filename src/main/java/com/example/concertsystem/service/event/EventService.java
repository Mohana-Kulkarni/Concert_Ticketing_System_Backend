package com.example.concertsystem.service.event;

import com.example.concertsystem.dto.EventResponse;
import com.example.concertsystem.entity.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface EventService {

    void addEvent(String name, String date, String description, String eventDuration, String venueId, List<String> userId, List<String> tierId) throws ExecutionException, InterruptedException;
    void updateEvent(String id, String name, String date, String description,  String eventDuration, String venueName, List<String> userId, List<String> tierId) throws ExecutionException, InterruptedException;
    List<EventResponse> getEventByArtist(String artist) throws ExecutionException, InterruptedException;
    List<EventResponse> getEventByPlace(String place) throws ExecutionException, InterruptedException;
    EventResponse getEventById(String id) throws ExecutionException, InterruptedException;
    List<EventResponse> getEventByVenue(String venue) throws ExecutionException, InterruptedException;
    String getEventIdByName(String eventName) throws ExecutionException, InterruptedException;

    List<EventResponse> getAllEvents() throws ExecutionException, InterruptedException;
    void deleteEventById(String id);


}
