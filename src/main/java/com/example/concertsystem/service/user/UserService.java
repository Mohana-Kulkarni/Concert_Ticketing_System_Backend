package com.example.concertsystem.service.user;

import com.example.concertsystem.dto.UserResponse;
import com.example.concertsystem.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface UserService {
    boolean addUser(String name, String userName, String userEmail, String profileImg, String walletId, String transactionId);
    UserResponse isUserRegistered(String walletId);
    UserResponse getUserById(String id);
    boolean updateUserInfo(String id, String name, String userName, String userEMail, String profileImg, String walletId,String transactionId);
    boolean deleteUser(String id);
    String getIdByUserName(String userName);

}
