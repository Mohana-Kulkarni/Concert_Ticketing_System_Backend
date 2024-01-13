package com.example.concertsystem.service.venue;

import com.example.concertsystem.entity.Venue;
import com.faunadb.client.types.Value;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface VenueService {

    void addVenue(Venue venue);
    Venue getVenueById(String id) throws ExecutionException, InterruptedException;
    Venue getVenueByName(String name) throws ExecutionException, InterruptedException;
//    List<Venue> getVenuesByPlace(String place) throws ExecutionException, InterruptedException;
    void updateVenueById(String id, Venue venue) throws ExecutionException, InterruptedException;
    void deleteVenueById(String id);

}
