package com.example.concertsystem.service.venue;

import com.example.concertsystem.entity.Venue;

public interface VenueService {

    void addVenue(Venue venue);
    Venue getVenueById(String id);
    Venue getVenueByPlace(String place);
    Venue updateVenue(String id, Venue venue);

    void deleteVenueById(String id);
}
