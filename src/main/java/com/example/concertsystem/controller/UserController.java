package com.example.concertsystem.controller;

import com.example.concertsystem.constants.GlobalConstants;
import com.example.concertsystem.dto.SuccessResponse;
import com.example.concertsystem.dto.UserResponse;
import com.example.concertsystem.entity.User;
import com.example.concertsystem.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;


@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/id")
    public ResponseEntity<UserResponse> getUserById(@Valid @RequestParam("id") String id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
    }

    @GetMapping("/registration")
    public ResponseEntity<UserResponse> checkUserRegistration(@Valid @RequestParam("walletId") String walletId){
        return ResponseEntity.status(HttpStatus.OK).body(userService.isUserRegistered(walletId));

    }

    @PostMapping("/")
    public ResponseEntity<SuccessResponse> addUser(@Valid @RequestBody User user){
        boolean result = userService.addUser(user.name(), user.userName(), user.userEmail(), user.profileImg(), user.walletId(), user.transactionId());
        if(result){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessResponse(GlobalConstants.STATUS_201, GlobalConstants.MESSAGE_201_User));
        }
        else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new SuccessResponse(GlobalConstants.STATUS_417, GlobalConstants.MESSAGE_417_POST));
        }
    }

    @PutMapping("/id")
    public ResponseEntity<SuccessResponse> updateUserById(@Valid @RequestParam("id") String id, @RequestBody User user){
        boolean result = userService.updateUserInfo(id, user.name(), user.userName(), user.userEmail(), user.profileImg(), user.walletId(), user.transactionId());
        if(result) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessResponse(GlobalConstants.STATUS_200, GlobalConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new SuccessResponse(GlobalConstants.STATUS_417, GlobalConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/delete/id")
    public ResponseEntity<SuccessResponse> deletePlaceById(@Valid @RequestParam("id") String id){
        boolean result = userService.deleteUser(id);
        if(result) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessResponse(GlobalConstants.STATUS_200, GlobalConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new SuccessResponse(GlobalConstants.STATUS_417, GlobalConstants.MESSAGE_417_DELETE));
        }
    }
}
