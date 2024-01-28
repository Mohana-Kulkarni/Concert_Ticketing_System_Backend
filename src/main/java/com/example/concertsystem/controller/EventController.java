package com.example.concertsystem.controller;

import com.example.concertsystem.dto.EventImageResponse;
import com.example.concertsystem.dto.EventResponse;
import com.example.concertsystem.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;


    @GetMapping("/id")
    public EventResponse getEventById(@RequestParam("id") String id) throws ExecutionException, InterruptedException, IOException {
        return eventService.getEventById(id);
    }

    @GetMapping("/city")
    public List<EventResponse> getEventByName(@RequestParam("city") String  city) throws ExecutionException, InterruptedException, IOException {
        return eventService.getEventByPlaceName(city).getList();

    }
    @GetMapping("/venue")
    public List<EventResponse> getEventByVenue(@RequestParam("venue") String venue) throws ExecutionException, InterruptedException, IOException {
        return eventService.getEventByVenueName(venue).getList();
    }
    @GetMapping("/artist")
    public List<EventResponse> getEventByArtist(@RequestParam("artist") String artist) throws ExecutionException, InterruptedException, IOException {
        return eventService.getEventByArtistName(artist).getList();
    }
    @PostMapping(value = "/")
    public void addEvent(@RequestBody EventImageResponse eventImageResponse) throws ExecutionException, InterruptedException {
        eventService.addEvent2(eventImageResponse.getEvent() ,eventImageResponse.getImgUrls(), eventImageResponse.getProfileImgUrls());
    }

    @PutMapping("/update/id")
    public void updateEventById(@RequestParam("id") String id, @RequestBody EventImageResponse event) throws ExecutionException, InterruptedException {
        eventService.updateEvent(id, event.getEvent(), event.getImgUrls(), event.getProfileImgUrls());
    }

    @DeleteMapping("/delete/id")
    public void deleteEventById(@RequestParam("id") String id) throws ExecutionException, InterruptedException {
        eventService.deleteEventById(id);
    }
}
