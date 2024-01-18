package com.example.concertsystem.controller;

import com.example.concertsystem.entity.Venue;
import com.example.concertsystem.service.venue.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/venues")
public class VenueController {

    @Autowired
    private VenueService venueService;

    @GetMapping("/id")
    public Venue getVenueById(@RequestParam("id") String id) throws ExecutionException, InterruptedException {
        return venueService.getVenueById(id);
    }

    @GetMapping("/name")
    public Venue getVenueByName(@RequestParam("name") String name) throws ExecutionException, InterruptedException {
        return venueService.getVenueByName(name);
    }
    @GetMapping("/city")
    public List<Venue> getVenueByCity(@RequestParam("city") String city) throws ExecutionException, InterruptedException {
        return venueService.getVenueByPlace(city);
    }

    @PostMapping("/")
    public void addVenue(@RequestBody Venue venue) {
        venueService.addVenue(venue);
    }

    @PutMapping("/id")
    public void updateVenueById(@RequestParam("id") String id, @RequestBody Venue venue) throws ExecutionException, InterruptedException {
        venueService.updateVenueById(id, venue.name(), venue.address(), venue.capacity(), venue.placeId());
    }

    @DeleteMapping("/id")
    public void deleteVenueById(@RequestParam("id") String id) {
        venueService.deleteVenueById(id);
    }
}
