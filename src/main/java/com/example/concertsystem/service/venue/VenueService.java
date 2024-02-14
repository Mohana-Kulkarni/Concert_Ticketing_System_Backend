package com.example.concertsystem.service.venue;

import com.example.concertsystem.entity.Venue;
import com.faunadb.client.types.Value;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface VenueService {

    String addVenue(Venue venue);
    Venue getVenueById(String id);
    Venue getVenueByName(String name);
    List<String> getVenueIdsByPlaceName(String placeName);
    String getVenueIdByVenueName(String venueName);
    List<Venue> getVenueByPlace(String place) ;
    boolean updateVenueById(String id, String name, String address, int capacity, String placeId);
    boolean deleteVenueById(String id);

}
