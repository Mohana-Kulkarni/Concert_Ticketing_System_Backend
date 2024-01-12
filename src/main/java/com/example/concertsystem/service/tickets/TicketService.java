package com.example.concertsystem.service.tickets;

import com.example.concertsystem.entity.Seats;
import com.example.concertsystem.entity.Ticket;
import com.example.concertsystem.entity.User;

public interface TicketService {
    void generateTicket(User user, Seats seats);
    Ticket updateTicket(String id, Ticket ticket);
    Ticket getTicketById(String id);
    Ticket getTicketByUserId(String user_id);
    void deleteTicketById(String id);
}
