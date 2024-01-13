package com.example.concertsystem.service.seats;

import com.example.concertsystem.entity.Seats;
import com.example.concertsystem.service.event.EventService;
import com.example.concertsystem.service.tier.TierService;
import com.example.concertsystem.service.user.UserService;
import com.faunadb.client.FaunaClient;
import org.springframework.stereotype.Service;

@Service
public class SeatsServiceImpl implements SeatsService{
    private FaunaClient faunaClient;
    private TierService tierService;
    private EventService eventService;
    public SeatsServiceImpl(FaunaClient faunaClient, TierService tierService, EventService eventService) {
        this.faunaClient = faunaClient;
        this.tierService = tierService;
        this.eventService = eventService;
    }
    @Override
    public void addSeats(boolean isBooked, String tierId, String eventId) {

    }

    @Override
    public Seats getSeatsByTier(String tier_name) {
        return null;
    }

    @Override
    public Seats getEmptySeatsOfTier(String tier_name) {
        return null;
    }

    @Override
    public Seats getBookedSeatsOfTier(String tier_name) {
        return null;
    }

    @Override
    public void updateSeatsById(String id, boolean isBooked, String tierId, String eventId) {

    }

    @Override
    public void updateSeatsStatusById(String id, boolean isBooked, String tierId, String eventId) {

    }

    @Override
    public void deleteSeatById(String id) {

    }
}
