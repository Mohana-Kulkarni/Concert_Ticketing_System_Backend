package com.example.concertsystem.controller;

import com.example.concertsystem.dto.EventResponse;
import com.example.concertsystem.entity.Event;
import com.example.concertsystem.entity.Place;
import com.example.concertsystem.service.event.EventService;
import com.example.concertsystem.service.place.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/id/{id}")
    public EventResponse getEventById(@PathVariable String id) throws ExecutionException, InterruptedException {
        return eventService.getEventById(id);
    }

    @GetMapping("/city/{name}")
    public List<EventResponse> getEventByName(@PathVariable String name) throws ExecutionException, InterruptedException {
        return eventService.getEventByPlace(name);
    }

    @GetMapping("/venue/{name}")
    public List<EventResponse> getEventByVenue(@PathVariable String name) throws ExecutionException, InterruptedException {
        return eventService.getEventByVenue(name);
    }
    @GetMapping("/artist/{name}")
    public List<EventResponse> getEventByArtist(@PathVariable String name) throws ExecutionException, InterruptedException {
        return eventService.getEventByArtist(name);
    }
    @PostMapping("/")
    public void addEvent(@RequestBody Event event) throws ExecutionException, InterruptedException {
        System.out.println(event);
        eventService.addEvent(event.name(), event.dateAndTime(), event.description(), event.eventDuration(),event.venueId(), event.userId(), event.tierId());
    }

    @PutMapping("/update/id/{id}")
    public void updateEventById(@PathVariable String id, @RequestBody Event event) throws ExecutionException, InterruptedException {
        eventService.updateEvent(id, event.name(), event.dateAndTime(), event.description(), event.eventDuration(), event.venueId(), event.userId(), event.tierId());
    }

    @DeleteMapping("/delete/id/{id}")
    public void deleteEventById(@PathVariable String id) {
        eventService.deleteEventById(id);
    }
}
