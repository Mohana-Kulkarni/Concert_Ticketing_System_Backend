package com.example.concertsystem.service.user;

import com.example.concertsystem.dto.UserResponse;
import com.example.concertsystem.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserService {
    void addUser(String name, String userName, String userEmail, String profileImg, String walletId, String transactionId) throws IOException;
    UserResponse isUserRegistered(String walletId) throws ExecutionException, InterruptedException;
    UserResponse getUserById(String id) throws ExecutionException, InterruptedException;
    List<UserResponse> getUsersByType(String role);
    void updateUserInfo(String id, String name, String userName, String userEMail, String profileImg, String walletId,String transactionId) throws ExecutionException, InterruptedException, IOException;
//    void updateUserRole(String id, String name) throws ExecutionException, InterruptedException;
    void deleteUser(String id);
    List<String> getUserIdsByUserName(List<String> userName);
    String getIdByUserName(String userName);

    List<UserResponse> getUserListById(List<String> userId) throws ExecutionException, InterruptedException;
}
