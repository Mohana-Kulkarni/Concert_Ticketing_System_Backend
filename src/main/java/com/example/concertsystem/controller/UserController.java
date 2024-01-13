package com.example.concertsystem.controller;

import com.example.concertsystem.entity.Place;
import com.example.concertsystem.entity.User;
import com.example.concertsystem.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/id={id}")
    public User getUserById(@PathVariable String id) throws ExecutionException, InterruptedException {
        return userService.getUserById(id);
    }

    @GetMapping("/role={role}")
    public List<User> getUserByRole(@PathVariable String role) throws ExecutionException, InterruptedException {
        return userService.getUsersByType(role);
    }

    @PostMapping("/")
    public void addUser(@RequestBody User user) {
        userService.addUser(user.name(), user.typeOfUser(), user.userName(), user.profileImg());
    }

    @PutMapping("/{id}")
    public void updateUserById(@PathVariable String id, @RequestBody User user) throws ExecutionException, InterruptedException {
        userService.updateUserInfo(id, user.name(),user.typeOfUser());
    }

    @PutMapping("/id={id}")
    public void updateUserRoleById(@PathVariable String id, @RequestBody User user) throws ExecutionException, InterruptedException {
        userService.updateUserRole(id, user.name(), user.typeOfUser());
    }

    @DeleteMapping("/{id}")
    public void deletePlaceById(@PathVariable String id) {
        userService.deleteUser(id);
    }
}
