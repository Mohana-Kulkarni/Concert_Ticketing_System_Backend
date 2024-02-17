package com.example.concertsystem.service.organiser;

import com.example.concertsystem.dto.EventResponse;
import com.example.concertsystem.dto.OrganiserResponse;
import com.example.concertsystem.entity.Organiser;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface OrganiserService {
    boolean addOrganiser(String name, String email, String govId, String walletId, String transactionId);
    OrganiserResponse isOrganiserRegistered(String walletId);
    OrganiserResponse getOrganiserById(String id);
    boolean updateOrganiser(String id, String eventId);
    boolean deleteOrganiser(String id);
}
