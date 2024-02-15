package com.example.concertsystem.service.place;

import com.example.concertsystem.dto.PlaceResponse;
import com.example.concertsystem.entity.Place;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PlaceService {
    boolean addPlace(String name);
    PlaceResponse getPlaceById(String id);
    PlaceResponse getPlaceByName(String name);
    List<PlaceResponse> getAllPlaces();
    boolean updatePlaceById(String id, String name, int count);
    void updateEventCount(String name) throws ExecutionException, InterruptedException;
    boolean deletePlaceById(String id);
    String getPlaceIdByPlaceName(String placeName);

}
