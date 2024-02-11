package com.example.concertsystem.service.place;

import com.example.concertsystem.entity.Place;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PlaceService {
    boolean addPlace(String name);
    Place getPlaceById(String id);
    Place getPlaceByName(String name);
    List<Place> getAllPlaces();
    boolean updatePlaceById(String id, String name);
    boolean deletePlaceById(String id);
    String getPlaceIdByPlaceName(String placeName);

}
