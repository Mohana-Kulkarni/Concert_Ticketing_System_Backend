package com.example.concertsystem.service.seats;

import com.example.concertsystem.entity.Seats;

public interface SeatsService {
    void addSeats(Seats seat);
    Seats getSeatsByTier(String tier_name);
    Seats getEmptySeatsOfTier(String tier_name);
    Seats getBookedSeatsOfTier(String tier_name);
    Seats updateSeatsById(String id, Seats seats);
    void deleteSeatById(String id);
}
