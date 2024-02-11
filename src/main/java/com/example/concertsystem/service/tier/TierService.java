package com.example.concertsystem.service.tier;
import com.example.concertsystem.entity.Ticket;
import com.example.concertsystem.entity.Tier;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface TierService {
    String addTier(String name, int capacity, int price);
    List<String> addNewTiers(List<Tier> tierList);
    Tier getTierById(String id);
    Tier getTierByName(String name);
//    int getAvailableSeats();
    boolean updateTier(String id, String name, int capacity, int price);
    boolean deleteTierById(String id);
    List<String> getIdByTierName(List<String> tierName);
    String getTierIdByTierName(String tierName);
    List<Tier> getTierListByIds(List<String> tierId);
}
