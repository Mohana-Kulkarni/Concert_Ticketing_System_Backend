package com.example.concertsystem.controller;

import com.example.concertsystem.dto.EventResponse;
import com.example.concertsystem.entity.Event;
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
        eventService.getEventByPlaceName(city);
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
    @PostMapping("/")
    public void addEvent(@RequestBody Event event) throws ExecutionException, InterruptedException, IOException {
        System.out.println(event);
        eventService.addEvent2(event.name(), event.dateAndTime(), event.description(), event.eventDuration(), event.images(), event.venueId(),event.userId(), event.tierId());
    }

    @PostMapping("/new/")
    public void addNewEvent(@RequestBody Event event) throws ExecutionException, InterruptedException, IOException {
        eventService.addEvent(event.name(), event.dateAndTime(), event.description(), event.eventDuration(), event.images(), event.venueId(),event.userId(), event.tierId());
    }

    @PutMapping("/update/id")
    public void updateEventById(@RequestParam("id") String id, @RequestBody Event event) throws ExecutionException, InterruptedException {
//        eventService.updateEvent(id, event.name(), event.dateAndTime(), event.description(), event.eventDuration(), event.venueId(), event.userId(), event.tierId());
    }

    @DeleteMapping("/delete/id")
    public void deleteEventById(@RequestParam("id") String id) throws ExecutionException, InterruptedException {
        eventService.deleteEventById(id);
    }
}
