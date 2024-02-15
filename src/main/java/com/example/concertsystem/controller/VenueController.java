package com.example.concertsystem.controller;

import com.example.concertsystem.constants.GlobalConstants;
import com.example.concertsystem.dto.EventResponse;
import com.example.concertsystem.dto.SuccessResponse;
import com.example.concertsystem.entity.Venue;
import com.example.concertsystem.service.venue.VenueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/venues")
@Validated
public class VenueController {

    @Autowired
    private VenueService venueService;

    @GetMapping("/id")
    public ResponseEntity<Venue> getVenueById(@RequestParam("id") String id){
        return ResponseEntity.status(HttpStatus.OK).body(venueService.getVenueById(id));
    }

    @GetMapping("/name")
    public ResponseEntity<Venue> getVenueByName(@RequestParam("name") String name) throws ExecutionException, InterruptedException {
        return ResponseEntity.status(HttpStatus.OK).body(venueService.getVenueByName(name));
    }
    @GetMapping("/city")
    public ResponseEntity<List<Venue>> getVenueByCity(@RequestParam("city") String city) throws ExecutionException, InterruptedException {
        return ResponseEntity.status(HttpStatus.OK).body(venueService.getVenueByPlace(city));

    }

    @PostMapping("/")
    public ResponseEntity<SuccessResponse> addVenue(@Valid @RequestBody Venue venue) {
        String result = venueService.addVenue(venue);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new SuccessResponse(GlobalConstants.STATUS_201, result));
    }

    @PutMapping("/id")
    public ResponseEntity<SuccessResponse> updateVenueById(@RequestParam("id") String id, @Valid @RequestBody Venue venue) throws ExecutionException, InterruptedException {
        boolean result = venueService.updateVenueById(id, venue.name(), venue.address(), venue.capacity(), venue.placeId());
        if(result) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessResponse(GlobalConstants.STATUS_200, GlobalConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new SuccessResponse(GlobalConstants.STATUS_417, GlobalConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/id")
    public ResponseEntity<SuccessResponse> deleteVenueById(@RequestParam("id") String id) {
        boolean result = venueService.deleteVenueById(id);
        if(result) {
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
