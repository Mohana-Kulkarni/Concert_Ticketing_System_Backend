package com.example.concertsystem.controller;


import com.example.concertsystem.entity.Ticket;
import com.example.concertsystem.service.tickets.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping("/id?{id}")
    public Ticket getTicketById(@PathVariable String id) throws ExecutionException, InterruptedException {
        return ticketService.getTicketById(id);
    }

    @GetMapping("/user?{name}")
    public Ticket getTicketByName(@PathVariable String name) throws ExecutionException, InterruptedException {
        return ticketService.getTicketByUserName(name);
    }

    @PostMapping("/")
    public void bookTicket(@RequestBody Ticket ticket) throws ExecutionException, InterruptedException {
        ticketService.generateTicket(ticket.count(),ticket.userId(), ticket.tierId(), ticket.eventId());
    }

    @PutMapping("/id?{id}")
    public void updateTicketById(@PathVariable String id, @RequestBody Ticket ticket) throws ExecutionException, InterruptedException {
        ticketService.updateTicket(id, ticket.count(),ticket.userId(), ticket.tierId(), ticket.eventId());
    }

    @DeleteMapping("/id?{id}")
    public void deleteTicketById(@PathVariable String id) {
        ticketService.deleteTicketById(id);
    }
}
