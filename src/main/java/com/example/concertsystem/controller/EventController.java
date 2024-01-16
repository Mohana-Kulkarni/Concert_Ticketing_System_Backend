package com.example.concertsystem.controller;

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

    @GetMapping("/id?{id}")
    public Event getEventById(@PathVariable String id) throws ExecutionException, InterruptedException {
        return eventService.getEventById(id);
    }

    @GetMapping("/city?{name}")
    public List<Event> getEventByName(@PathVariable String name) throws ExecutionException, InterruptedException {
        return eventService.getEventByPlace(name);
    }

    @GetMapping("/venue?{name}")
    public List<Event> getEventByVenue(@PathVariable String name) throws ExecutionException, InterruptedException {
        return eventService.getEventByVenue(name);
    }
    @GetMapping("/artist?{name}")
    public List<Event> getEventByArtist(@PathVariable String name) throws ExecutionException, InterruptedException {
        return eventService.getEventByArtist(name);
    }
    @PostMapping("/")
    public void addEvent(@RequestBody Event event) throws ExecutionException, InterruptedException {
        eventService.addEvent(event.name(), event.dateAndTime(), event.description(), event.eventDuration(),event.venueId(), event.userId(), event.tierId());
    }

    @PutMapping("/id?{id}")
    public void updateEventById(@PathVariable String id, @RequestBody Event event) throws ExecutionException, InterruptedException {
        eventService.updateEvent(id, event.name(), event.dateAndTime(), event.description(), event.eventDuration(), event.venueId(), event.userId(), event.tierId());
    }

    @DeleteMapping("/id?{id}")
    public void deleteEventById(@PathVariable String id) {
        eventService.deleteEventById(id);
    }
}
