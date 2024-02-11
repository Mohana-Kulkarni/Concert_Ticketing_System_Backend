package com.example.concertsystem.controller;

import com.example.concertsystem.constants.GlobalConstants;
import com.example.concertsystem.dto.SuccessResponse;
import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.service.tier.TierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/tiers")
@Validated
public class TierController {
    @Autowired
    private TierService tierService;

    @GetMapping("/id")
    public ResponseEntity<Tier> getTierById(@RequestParam("id") String id){
        return ResponseEntity.status(HttpStatus.OK).body(tierService.getTierById(id));
    }

    @GetMapping("/name")
    public ResponseEntity<Tier> getTierByName(@RequestParam("name") String name){
        return ResponseEntity.status(HttpStatus.OK).body(tierService.getTierByName(name));
    }

    @PostMapping("/")
    public ResponseEntity<String> addTier(@Valid @RequestBody Tier tier){
        return ResponseEntity.status(HttpStatus.CREATED).body(tierService.addTier(tier.name(), tier.capacity(), tier.price()));
    }

    @PutMapping("/id")
    public ResponseEntity<SuccessResponse> updateTierById(@RequestParam("id") String id, @Valid @RequestBody Tier tier){
        boolean result = tierService.updateTier(id, tier.name(),tier.capacity(), tier.price());
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
    public ResponseEntity<SuccessResponse> deleteTierById(@RequestParam("id") String id) {
        boolean result = tierService.deleteTierById(id);
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
