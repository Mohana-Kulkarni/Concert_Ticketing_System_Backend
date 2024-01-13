package com.example.concertsystem.service.venue;

import com.example.concertsystem.entity.Venue;
import com.faunadb.client.types.Value;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface VenueService {

    void addVenue(Venue venue);
    Venue getVenueById(String id) throws ExecutionException, InterruptedException;
    Venue getVenueByName(String name) throws ExecutionException, InterruptedException;
    List<String> getVenueIdsByPlaceName(String placeName) throws ExecutionException, InterruptedException;
    String getVenueIdByVenueName(String venueName);
    List<Venue> getVenueByPlace(String place) throws ExecutionException, InterruptedException;
    void updateVenueById(String id, String name, String address, int capacity, String placeId) throws ExecutionException, InterruptedException;
    void deleteVenueById(String id);

}
