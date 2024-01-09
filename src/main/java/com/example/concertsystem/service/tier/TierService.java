package com.example.concertsystem.service.tier;
import com.example.concertsystem.entity.Tier;
public interface TierService {
    void addTier(Tier tier);
    Tier getTierById(String id);
    Tier getTierByName(String name);
    Tier getTierByEvent(String event_name);

    Tier updateTier(String id, Tier tier);
    void deleteTierById(String id);
}
