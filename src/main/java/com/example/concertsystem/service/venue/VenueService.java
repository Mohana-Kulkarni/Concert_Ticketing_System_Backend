package com.example.concertsystem.service.venue;

import com.example.concertsystem.entity.Venue;
import com.faunadb.client.types.Value;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface VenueService {

    void addVenue(String name, String address, int capacity, String place_id);
    Venue getVenueById(String id) throws ExecutionException, InterruptedException;
    List<Venue> getVenuesByPlace(String place) throws ExecutionException, InterruptedException;
    void updateVenueById(String id, String name, String address, int capacity, String place_id) throws ExecutionException, InterruptedException;
    void deleteVenueById(String id);
}
