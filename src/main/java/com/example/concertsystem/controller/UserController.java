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

import java.util.Map;


@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/id")
    public ResponseEntity<UserResponse> getUserById(@RequestParam("id") String id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
    }

    @GetMapping("/registration")
    public ResponseEntity<UserResponse> checkUserRegistration(@RequestParam("walletId") String walletId){
        return ResponseEntity.status(HttpStatus.OK).body(userService.isUserRegistered(walletId));

    }

    @PostMapping("/")
    public ResponseEntity<SuccessResponse> addUser(@Valid @RequestBody User user){
        Map<String, String> result = userService.addUser(user.userEmail(), user.profileImg(), user.walletId(), user.transactionId());
        if(result.get("result").equals("true")){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessResponse(GlobalConstants.STATUS_201, result.get("id")));
        }
        else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new SuccessResponse(GlobalConstants.STATUS_417, GlobalConstants.MESSAGE_417_POST));
        }
    }

    @PutMapping("/id")
    public ResponseEntity<SuccessResponse> updateUserById(@RequestParam("id") String id, @Valid @RequestBody UserResponse user){
        boolean result = userService.updateUserInfo(id, user.userEmail(), user.profileImg(), user.walletId(), user.transactionId(), user.userDetailsId());
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

    @PutMapping("/details/")
    public ResponseEntity<SuccessResponse> updateUserDetailsId(@RequestParam("id") String id, @RequestParam("detailsId") String detailsId) {
        boolean result = userService.updateUserDetailsId(id, detailsId);
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
    public ResponseEntity<SuccessResponse> deletePlaceById(@RequestParam("id") String id){
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
