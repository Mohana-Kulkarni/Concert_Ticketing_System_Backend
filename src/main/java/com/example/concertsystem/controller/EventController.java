package com.example.concertsystem.controller;

import com.example.concertsystem.dto.EventResponse;
import com.example.concertsystem.entity.Artist;
import com.example.concertsystem.entity.Event;
import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public void addEvent(@RequestParam("name") String name,
                         @RequestParam("dateAndTime") String dateAndTime,
                         @RequestParam("description") String description,
                         @RequestParam("eventDuration") String eventDuration,
                         @RequestParam("images") List<MultipartFile> images,
                         @RequestParam("venueId") String venueId,
                         @RequestParam("artistList") List<Artist> artistList,
                         @RequestParam("tierList") List<Tier> tierList) throws ExecutionException, InterruptedException, IOException {
        System.out.println(artistList);
        System.out.println(tierList);
        System.out.println(images);
//        eventService.addEvent2(name, dateAndTime, description, eventDuration, venueId, artistList, tierList, images);
    }

    @PostMapping("/new/")
    public void addNewEvent(@RequestParam("name") String name,
                            @RequestParam("dateAndTime") String dateAndTime,
                            @RequestParam("description") String description,
                            @RequestParam("eventDuration") String eventDuration,
                            @RequestParam("images") List<MultipartFile> images,
                            @RequestParam("venueId") String venueId,
                            @RequestParam("artistList") List<Artist> artistList,
                            @RequestParam("tierList") List<Tier> tierList) throws ExecutionException, InterruptedException, IOException {
        eventService.addEvent(name, dateAndTime, description, eventDuration, venueId, artistList, tierList, images);
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
