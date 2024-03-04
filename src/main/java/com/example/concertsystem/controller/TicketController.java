package com.example.concertsystem.controller;


import com.example.concertsystem.constants.GlobalConstants;
import com.example.concertsystem.dto.SuccessResponse;
import com.example.concertsystem.dto.TicketResponse;
import com.example.concertsystem.entity.Ticket;
import com.example.concertsystem.service.tickets.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/tickets")
@Validated
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping("/id")
    public ResponseEntity<TicketResponse> getTicketById(@RequestParam("id") String id){
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.getTicketById(id));
    }

    @GetMapping("/user")
    public ResponseEntity<List<TicketResponse>> getTicketByName(@RequestParam("user") String user){
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.getTicketByUserId(user));
    }

    @PostMapping("/")
    public ResponseEntity<SuccessResponse> bookTicket(@Valid @RequestBody Ticket ticket){
        boolean result = ticketService.generateTicket(ticket.count(),ticket.userId(), ticket.tierId(), ticket.eventId(), ticket.transactionId(),ticket.nftToken());
        if(result){
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new SuccessResponse(GlobalConstants.STATUS_201, GlobalConstants.MESSAGE_201_Ticket));
        }
        else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new SuccessResponse(GlobalConstants.STATUS_417, GlobalConstants.MESSAGE_417_POST));
        }
    }

    @PutMapping("/id")
    public ResponseEntity<SuccessResponse> updateTicketById(@RequestParam("id") String id, @Valid @RequestBody Ticket ticket){
        boolean result = ticketService.updateTicket(id, ticket.count(),ticket.userId(), ticket.tierId(), ticket.eventId(), ticket.transactionId(),ticket.nftToken());
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
    public ResponseEntity<SuccessResponse> deleteTicketById(@RequestParam("id") String id) {
        boolean result = ticketService.deleteTicketById(id);
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
