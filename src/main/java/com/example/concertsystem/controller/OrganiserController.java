package com.example.concertsystem.controller;

import com.example.concertsystem.constants.GlobalConstants;
import com.example.concertsystem.dto.SuccessResponse;
import com.example.concertsystem.entity.Organiser;
import com.example.concertsystem.service.organiser.OrganiserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/organisers")
@Validated
public class OrganiserController {

    private OrganiserService organiserService;
    public OrganiserController(OrganiserService organiserService) {

        this.organiserService = organiserService;
    }

    @GetMapping("/id")
    public ResponseEntity<Organiser> getOrganiser(@RequestParam("id") String id){
        return ResponseEntity.status(HttpStatus.OK).body(organiserService.getOrganiserById(id));
    }

    @PostMapping("/")
    public ResponseEntity<SuccessResponse> addNewOrganiser(@Valid  @RequestBody Organiser organiser) {
        boolean result = organiserService.addOrganiser(organiser.name(), organiser.userName(),
                organiser.email(), organiser.govId(), organiser.walletId(), organiser.transactionId());
        if(result){
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new SuccessResponse(GlobalConstants.STATUS_201, GlobalConstants.MESSAGE_201_Organiser));
        }
        else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new SuccessResponse(GlobalConstants.STATUS_417, GlobalConstants.MESSAGE_417_POST));
        }
    }

    @PutMapping("/update/id")
    public ResponseEntity<SuccessResponse> updateOrganiser(@RequestParam("id") String id,@Valid @RequestBody Organiser organiser){
        boolean result = organiserService.updateOrganiser(id, organiser.name(), organiser.userName(),
                organiser.email(), organiser.govId(), organiser.walletId(), organiser.transactionId());
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

    @DeleteMapping("/delete/id")
    public ResponseEntity<SuccessResponse> deleteOrganiser(@RequestParam("id") String id) {
        boolean result = organiserService.deleteOrganiser(id);
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
