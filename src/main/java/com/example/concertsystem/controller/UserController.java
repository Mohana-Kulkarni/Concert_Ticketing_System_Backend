package com.example.concertsystem.controller;

import com.example.concertsystem.dto.UserResponse;
import com.example.concertsystem.entity.Place;
import com.example.concertsystem.entity.User;
import com.example.concertsystem.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/id")
    public UserResponse getUserById(@RequestParam("id") String id) throws ExecutionException, InterruptedException {
        return userService.getUserById(id);
    }

    @GetMapping("/role")
    public List<UserResponse> getUserByRole(@RequestParam("role") String role) throws ExecutionException, InterruptedException {
        return userService.getUsersByType(role);
    }

    @PostMapping("/")
    public void addUser(@RequestParam("name") String name,
                        @RequestParam("userName") String userName,
                        @RequestParam("walletId") String walletId,
                        @RequestParam("userEmail") String userEmail,
                        @RequestPart("profileImg") MultipartFile profileImg) throws IOException {
        userService.addUser(name, userName, walletId, userEmail, profileImg);
    }

    @PutMapping("/id")
    public void updateUserById(@RequestParam("id") String id, @RequestBody User user) throws ExecutionException, InterruptedException, IOException {
        userService.updateUserInfo(id, user.name(), user.userName(), user.walletId(), user.userEmail(), user.profileImg());
    }

//    @PutMapping("/update/id")
//    public void updateUserRoleById(@RequestParam("id") String id, @RequestBody User user) throws ExecutionException, InterruptedException {
//        userService.updateUserRole(id, user.name(), user.typeOfUser());
//    }

    @DeleteMapping("/delete/id")
    public void deletePlaceById(@RequestParam("id") String id) {
        userService.deleteUser(id);
    }
}
