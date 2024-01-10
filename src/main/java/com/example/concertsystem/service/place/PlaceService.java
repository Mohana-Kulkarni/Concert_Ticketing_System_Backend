package com.example.concertsystem.service.place;

import com.example.concertsystem.entity.Place;

import java.util.concurrent.ExecutionException;

public interface PlaceService {
    void addPlace(String name);
    Place getPlaceById(String id) throws ExecutionException, InterruptedException;
    Place getPlaceByName(String name) throws ExecutionException, InterruptedException;
    void updatePlaceById(String id, String name) throws ExecutionException, InterruptedException;
    void deletePlaceById(String id);

}
