package com.example.concertsystem.controller;


import com.example.concertsystem.dto.TicketResponse;
import com.example.concertsystem.entity.Ticket;
import com.example.concertsystem.service.tickets.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping("/id")
    public TicketResponse getTicketById(@RequestParam("id") String id) throws ExecutionException, InterruptedException, IOException {
        return ticketService.getTicketById(id);
    }

    @GetMapping("/user")
    public TicketResponse getTicketByName(@RequestParam("user") String user) throws ExecutionException, InterruptedException, IOException {
        return ticketService.getTicketByUserName(user);
    }

    @PostMapping("/")
    public void bookTicket(@RequestBody Ticket ticket) throws ExecutionException, InterruptedException, IOException {
        ticketService.generateTicket(ticket.count(),ticket.userId(), ticket.tierId(), ticket.eventId());
    }

    @PutMapping("/id")
    public void updateTicketById(@RequestParam("id") String id, @RequestBody Ticket ticket) throws ExecutionException, InterruptedException, IOException {
        ticketService.updateTicket(id, ticket.count(),ticket.userId(), ticket.tierId(), ticket.eventId());
    }

    @DeleteMapping("/id")
    public void deleteTicketById(@RequestParam("id") String id) {
        ticketService.deleteTicketById(id);
    }
}
