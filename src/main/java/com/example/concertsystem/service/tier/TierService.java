package com.example.concertsystem.service.tier;
import com.example.concertsystem.entity.Tier;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface TierService {
    void addTier(String name, int capacity, int price);
    Tier getTierById(String id) throws ExecutionException, InterruptedException;
    Tier getTierByName(String name) throws ExecutionException, InterruptedException;
    void updateTier(String id, String name, int capacity, int price) throws ExecutionException, InterruptedException;
    void deleteTierById(String id);
    List<String> getIdByTierName(List<String> tierName);
}
