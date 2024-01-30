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

    void addEvent2(Event event, List<String> imageUrls, List<String> profileImgUrls) throws ExecutionException, InterruptedException;
    void updateEvent(String id, Event event, List<String> imageUrls, List<String> profileImgUrls) throws ExecutionException, InterruptedException;
    void updateEventScore(String eventId, double similarityScore);
    List<EventResponse> getEventByArtist(String artist) throws ExecutionException, InterruptedException, IOException;
    List<EventResponse> getEventByPlace(String place) throws ExecutionException, InterruptedException, IOException;
    EventResponse getEventById(String id) throws ExecutionException, InterruptedException, IOException;
    List<EventResponse> getEventByVenue(String venue) throws ExecutionException, InterruptedException, IOException;
    List<EventResponse> getSimilarEvents(String eventId) throws IOException, ExecutionException, InterruptedException;
    String getEventIdByName(String eventName) throws ExecutionException, InterruptedException;
//    String getTiersAddedForEvent(List<Tier> tierList) throws ExecutionException, InterruptedException;

    List<EventResponse> getAllEvents() throws ExecutionException, InterruptedException, IOException;
    void deleteEventById(String id) throws ExecutionException, InterruptedException;
    String getPlaceByEventId(String EventId);
    ListWrapper getEventByPlaceName(String place) throws ExecutionException, InterruptedException, IOException;
    ListWrapper getEventByArtistName(String artist) throws ExecutionException, InterruptedException, IOException;
    ListWrapper getEventByVenueName(String venue) throws ExecutionException, InterruptedException, IOException;



}
