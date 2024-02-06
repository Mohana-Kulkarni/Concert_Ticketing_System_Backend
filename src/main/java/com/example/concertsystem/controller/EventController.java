package com.example.concertsystem.controller;

import com.example.concertsystem.constants.GlobalConstants;
import com.example.concertsystem.dto.EventImageResponse;
import com.example.concertsystem.dto.EventResponse;
import com.example.concertsystem.dto.SuccessResponse;
import com.example.concertsystem.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/all")
    public List<EventResponse> getAllEvents() throws IOException, ExecutionException, InterruptedException {
        return eventService.getAllEvents();
    }

    @GetMapping("/relatedPosts")
    public List<EventResponse> getRelatedPosts(@RequestParam("id") String id) throws IOException, ExecutionException, InterruptedException {
        return eventService.getSimilarEvents(id);
    }
    @PostMapping(value = "/")
    public void addEvent(@RequestBody EventImageResponse eventImageResponse) throws ExecutionException, InterruptedException {
        eventService.addEvent2(eventImageResponse.getEvent() ,eventImageResponse.getImgUrls());
    }

    @PutMapping("/update/id")
    public void updateEventById(@RequestParam("id") String id, @RequestBody EventImageResponse event) throws ExecutionException, InterruptedException {
        eventService.updateEvent(id, event.getEvent(), event.getImgUrls());
    }

    @DeleteMapping("/delete/id")
    public ResponseEntity<SuccessResponse> deleteEventById(@RequestParam("id") String id) throws ExecutionException, InterruptedException {
        boolean isDeleted = eventService.deleteEventById(id);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessResponse(GlobalConstants.STATUS_200, GlobalConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new SuccessResponse(GlobalConstants.STATUS_417, GlobalConstants.MESSAGE_417_DELETE));
        }
    }
}
