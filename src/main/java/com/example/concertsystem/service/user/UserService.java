package com.example.concertsystem.service.user;

import com.example.concertsystem.entity.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserService {
    void addUser(String name, String role);
    User getUserById(String id) throws ExecutionException, InterruptedException;
    List<User> getUsersByType(String role);
    void updateUserInfo(String id, String name) throws ExecutionException, InterruptedException;
    void updateUserRole(String id, String role) throws ExecutionException, InterruptedException;
    void deleteUser(String id);
}
