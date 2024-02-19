package com.example.concertsystem.service.organiser;

import com.example.concertsystem.dto.EventImageResponse;
import com.example.concertsystem.dto.EventResponse;
import com.example.concertsystem.dto.OrganiserResponse;
import com.example.concertsystem.entity.Organiser;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface OrganiserService {
    boolean addOrganiser(String name, String email, String govId, String walletId, String transactionId, String profileImg);
    OrganiserResponse isOrganiserRegistered(String walletId);
    boolean createEvent(EventImageResponse eventImageResponse, String organiserId);
    List<EventResponse> getEventsAddedByOrganiser(String id);
    OrganiserResponse getOrganiserById(String id);
    boolean updateOrganiser(String id, String eventId);
    boolean updateOrganiserProfile(String id, Organiser organiser);
    boolean deleteOrganiser(String id);
}
