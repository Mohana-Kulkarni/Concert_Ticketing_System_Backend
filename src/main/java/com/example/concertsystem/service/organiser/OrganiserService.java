package com.example.concertsystem.service.organiser;

import com.example.concertsystem.entity.Organiser;

import java.util.concurrent.ExecutionException;

public interface OrganiserService {
    boolean addOrganiser(String name, String userName, String email, String govId, String walletId, String transactionId);
    Organiser getOrganiserById(String id);
    boolean updateOrganiser(String id, String name, String userName, String email, String govId, String walletId, String transactionId);
    boolean deleteOrganiser(String id);
}
