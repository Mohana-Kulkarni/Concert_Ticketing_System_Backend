package com.example.concertsystem.service.user;

import org.springframework.security.core.userdetails.User;

public interface UserService {
    void addUser(User user);
    User getUserById(String id);
    User updateUserInfo(String id, User user);
    User updateUserRole(String id, String role);
    void deleteUser(String id);
}
