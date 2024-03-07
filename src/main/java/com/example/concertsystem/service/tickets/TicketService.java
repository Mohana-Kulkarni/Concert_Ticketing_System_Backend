package com.example.concertsystem.service.tickets;

import com.example.concertsystem.dto.TicketResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface TicketService {
    boolean generateTicket(int count, String userId, String tierId, String eventId, String transactionId, List<String> ntfToken);
    boolean updateTicket(String id, String ntfId);
    TicketResponse getTicketById(String id);
    List<TicketResponse> getTicketByUserId(String walletId);
    boolean deleteTicketById(String id);
}
