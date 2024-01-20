package com.example.concertsystem.service.tier;
import com.example.concertsystem.entity.Ticket;
import com.example.concertsystem.entity.Tier;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface TierService {
    String addTier(String name, int capacity, int price) throws ExecutionException, InterruptedException;
    List<String> addNewTiers(List<Tier> tierList) throws ExecutionException, InterruptedException;
    Tier getTierById(String id) throws ExecutionException, InterruptedException;
    Tier getTierByName(String name) throws ExecutionException, InterruptedException;
//    int getAvailableSeats();
    void updateTier(String id, String name, int capacity, int price) throws ExecutionException, InterruptedException;
    void deleteTierById(String id);
    List<String> getIdByTierName(List<String> tierName);
    String getTierIdByTierName(String tierName);
    List<Tier> getTierListByIds(List<String> tierId) throws ExecutionException, InterruptedException;
    List<String> getTierByEventId(String eventId) throws ExecutionException, InterruptedException;

}
