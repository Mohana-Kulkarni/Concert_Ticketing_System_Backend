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

    @GetMapping("/id={id}")
    public Venue getVenueById(@PathVariable String id) throws ExecutionException, InterruptedException {
        return venueService.getVenueById(id);
    }

    @GetMapping("/name={name}")
    public Venue getVenueByName(@PathVariable String name) throws ExecutionException, InterruptedException {
        return venueService.getVenueByName(name);
    }
    @GetMapping("/city={city}")
    public List<Venue> getVenueByCity(@PathVariable String city) throws ExecutionException, InterruptedException {
        return venueService.getVenueByPlace(city);
    }

    @PostMapping("/")
    public void addVenue(@RequestBody Venue venue) {
        venueService.addVenue(venue);
    }

//    @PutMapping("/{id}")
//    public void updateVenueById(@PathVariable String id, @RequestBody Venue venue) throws ExecutionException, InterruptedException {
//        venueService.updateVenueById(id, venue);
//    }

    @DeleteMapping("/{id}")
    public void deleteVenueById(@PathVariable String id) {
        venueService.deleteVenueById(id);
    }
}
