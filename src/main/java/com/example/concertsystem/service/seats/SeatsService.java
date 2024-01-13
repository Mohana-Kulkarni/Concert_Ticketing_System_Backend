package com.example.concertsystem.service.seats;

import com.example.concertsystem.entity.Seats;

public interface SeatsService {
    void addSeats(boolean isBooked, String tierId, String eventId);
    Seats getSeatsByTier(String tier_name);
    Seats getEmptySeatsOfTier(String tier_name);
    Seats getBookedSeatsOfTier(String tier_name);
    void updateSeatsById(String id, boolean isBooked, String tierId, String eventId);
    void updateSeatsStatusById(String id, boolean isBooked, String tierId, String eventId);
    void deleteSeatById(String id);
}
