package com.example.concertsystem.service.event;

import com.example.concertsystem.dto.EventResponse;
import com.example.concertsystem.entity.Artist;
import com.example.concertsystem.entity.Event;
import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.Wrapper.ListWrapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface EventService {

    boolean addEvent2(Event event, List<String> imageUrls);
    void updateEvent(String id, Event event, List<String> imageUrls);
    List<EventResponse> getEventByArtist(String artist);
    List<EventResponse> getEventByPlace(String place);
    EventResponse getEventById(String id);
    List<EventResponse> getEventByVenue(String venue);
    List<EventResponse> getSimilarEvents(String eventId);
    String getEventIdByName(String eventName);
//    String getTiersAddedForEvent(List<Tier> tierList) throws ExecutionException, InterruptedException;

    List<EventResponse> getAllEvents();
    boolean deleteEventById(String id);
    String getPlaceByEventId(String EventId);
    ListWrapper getEventByPlaceName(String place);
    ListWrapper getEventByArtistName(String artist);
    ListWrapper getEventByVenueName(String venue);



}
