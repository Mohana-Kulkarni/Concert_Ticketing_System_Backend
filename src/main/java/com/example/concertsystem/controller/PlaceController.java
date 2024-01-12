package com.example.concertsystem.controller;


import com.example.concertsystem.entity.Place;
import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.service.place.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/places")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @GetMapping("/id={id}")
    public Place getPlaceById(@PathVariable String id) throws ExecutionException, InterruptedException {
        return placeService.getPlaceById(id);
    }

    @GetMapping("/city={name}")
    public Place getPlaceByName(@PathVariable String name) throws ExecutionException, InterruptedException {
        return placeService.getPlaceByName(name);
    }

    @PostMapping("/")
    public void addPlace(@RequestBody Place place) {
        System.out.println(place.city());
        placeService.addPlace(place.city());
    }

    @PutMapping("/{id}")
    public void updatePlaceById(@PathVariable String id, @RequestBody Place place) throws ExecutionException, InterruptedException {
        placeService.updatePlaceById(id, place.city());
    }

    @DeleteMapping("/{id}")
    public void deletePlaceById(@PathVariable String id) {
        placeService.deletePlaceById(id);
    }
}
