package com.example.concertsystem.service.organiser;

import com.example.concertsystem.entity.Organiser;

import java.util.concurrent.ExecutionException;

public interface OrganiserService {
    void addOrganiser(String name, String userName, String email, String govId, String walletId);

    Organiser getOrganiserById(String id) throws ExecutionException, InterruptedException;
    void updateOrganiser(String id, String name, String userName, String email, String govId, String walletId) throws ExecutionException, InterruptedException;
    void deleteOrganiser(String id);
}
