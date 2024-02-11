package com.example.concertsystem.controller;


import com.example.concertsystem.constants.GlobalConstants;
import com.example.concertsystem.dto.SuccessResponse;
import com.example.concertsystem.entity.Place;
import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.service.place.PlaceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/places")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @GetMapping("/id")
    public ResponseEntity<Place> getPlaceById(@RequestParam("id") String id){
        return ResponseEntity.status(HttpStatus.OK).body(placeService.getPlaceById(id));
    }

    @GetMapping("/city")
    public ResponseEntity<Place> getPlaceByName(@RequestParam("city") String city){
        return ResponseEntity.status(HttpStatus.OK).body(placeService.getPlaceByName(city));

    }

    @PostMapping("/")
    public ResponseEntity<SuccessResponse> addPlace(@Valid @RequestBody Place place) {
        boolean result = placeService.addPlace(place.city());
        if(result){
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new SuccessResponse(GlobalConstants.STATUS_201, GlobalConstants.MESSAGE_201_Place));
        }
        else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new SuccessResponse(GlobalConstants.STATUS_417, GlobalConstants.MESSAGE_417_DELETE));
        }
    }

    @PutMapping("/id")
    public ResponseEntity<SuccessResponse> updatePlaceById(@RequestParam("id") String id, @Valid @RequestBody Place place){
        boolean result = placeService.updatePlaceById(id, place.city());
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
    public ResponseEntity<SuccessResponse> deletePlaceById(@RequestParam("id") String id) {
        boolean result = placeService.deletePlaceById(id);
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
