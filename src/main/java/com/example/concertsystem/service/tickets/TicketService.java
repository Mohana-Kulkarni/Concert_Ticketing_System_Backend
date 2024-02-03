package com.example.concertsystem.service.tickets;

import com.example.concertsystem.dto.TicketResponse;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface TicketService {
    void generateTicket(int count, String userId, String tierId, String eventId, String transactionId, String ntfToken) throws ExecutionException, InterruptedException, IOException;
    void updateTicket(String id, int count, String userId, String tierId, String eventId, String transactionId, String ntfToken) throws ExecutionException, InterruptedException, IOException;
    TicketResponse getTicketById(String id) throws ExecutionException, InterruptedException, IOException;
    TicketResponse getTicketByUserId(String walletId) throws ExecutionException, InterruptedException, IOException;
    void deleteTicketById(String id);
}
