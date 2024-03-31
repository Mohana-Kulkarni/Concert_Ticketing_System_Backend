package com.example.concertsystem.service.user;

import com.example.concertsystem.dto.UserResponse;
import com.example.concertsystem.exception.ResourceNotFoundException;
import com.example.concertsystem.exception_handling.classes.UserNotFoundException;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.beans.factory.annotation.Autowired;

import static com.faunadb.client.query.Language.*;
import static com.faunadb.client.query.Language.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class UserServiceImpl implements UserService{
    private FaunaClient faunaClient;

    @Autowired
    public UserServiceImpl(FaunaClient faunaClient) {
        this.faunaClient= faunaClient;
    }
    @Override
    public Map<String, String> addUser(String userEmail, String profileImg, String walletId, String transactionId){
        Map<String, String > map = new HashMap<>();
        try {
            Map<String, Object> userData = new HashMap<>();
            userData.put("userEmail", userEmail);
            userData.put("walletId", walletId);
            userData.put("transactionId", transactionId);
            userData.put("profileImg", profileImg);
            userData.put("userDetailsId", new String());
            Value val = faunaClient.query(
                    Create(
                            Collection("User"),
                            Obj(
                                    "data",
                                    Value(userData)
                            )
                    )
            ).get();
            String id = val.at("ref").get(Value.RefV.class).getId();
            map.put("id", id);
            map.put("result", "true");
            return map;
        }catch (Exception e){
            map.put("result", "false");
            return map;
        }

    }

    @Override
    public UserResponse isUserRegistered(String walletId){
        try {
            Value val = faunaClient.query(
                    Get(
                            Match(Index("user_by_walletId"), Value(walletId))
                    )
            ).get();
            return new UserResponse(
                    val.at("ref").to(Value.RefV.class).get().getId(),
                    val.at("data", "userEmail").to(String.class).get(),
                    val.at("data","walletId").to(String.class).get(),
                    val.at("data", "transactionId").to(String.class).get(),
                    val.at("data", "profileImg").to(String.class).get(),
                    val.at("data", "userDetailsId").to(String.class).get()
            );
        } catch (Exception e) {
            throw new ResourceNotFoundException("User","walletId",walletId);
        }

    }

    @Override
    public UserResponse getUserById(String id) throws UserNotFoundException{
        try {
            Value res = faunaClient.query(Get(Ref(Collection("User"), id))).get();

            return new UserResponse(
                    res.at("ref").to(Value.RefV.class).get().getId(),
                    res.at("data", "userEmail").to(String.class).get(),
                    res.at("data", "walletId").to(String.class).get(),
                    res.at("data", "transactionId").to(String.class).get(),
                    res.at("data", "profileImg").to(String.class).get(),
                    res.at("data", "userDetailsId").to(String.class).get()
            );
        } catch (Exception e) {
            throw new ResourceNotFoundException("User","id",id);
        }
    }

    @Override
    public boolean updateUserInfo(String id, String userEmail, String profileImg, String walletId, String transactionId, String userDetailsId){
        try {
            getUserById(id);
            try {
                Map<String, Object> userData = new HashMap<>();
                userData.put("userEmail", userEmail);
                userData.put("walletId", walletId);
                userData.put("transactionId", transactionId);
                userData.put("profileImg", profileImg);
                userData.put("userDetailsId", userDetailsId);
                faunaClient.query(
                        Update(Ref(Collection("User"), id),
                                Obj(
                                        "data",
                                        Value(userData)
                                )
                        )
                ).get();
                return true;
            } catch (Exception e) {
                return false;
            }
        }catch (Exception e) {
            throw new ResourceNotFoundException("User", "id", id);
        }
    }

    @Override
    public boolean updateUserDetailsId(String id, String userDetailsId) {
        try{
            UserResponse user = getUserById(id);
            try {
                Map<String, Object> userData = new HashMap<>();
                userData.put("userEmail", user.userEmail());
                userData.put("walletId", user.walletId());
                userData.put("transactionId", user.transactionId());
                userData.put("profileImg", user.profileImg());
                userData.put("userDetailsId", userDetailsId);
                faunaClient.query(
                        Update(Ref(Collection("User"), id),
                                Obj(
                                        "data",
                                        Value(userData)
                                )
                        )
                ).get();
                return true;
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("User", "id", id);
        }
    }

    @Override
    public boolean deleteUser(String id) throws UserNotFoundException {

       try {
           getUserById(id);
           try {
               Value val = faunaClient.query(Delete(Ref(Collection("User"), id))).get();
               if (val == null)
                   return false;
               return true;
           } catch (Exception e) {
               return false;
           }

       } catch (Exception e) {
           throw new ResourceNotFoundException("User", "id", id);
       }
    }

    @Override
    public String getIdByUserName(String userName){
        try {
            String value = faunaClient.query(Get(Match(Index("user_by_username"),
                    Value(userName)))).join().at("ref").get(Value.RefV.class).getId();

            return value;
        }catch(Exception e){
            throw new ResourceNotFoundException("User","Username",userName);
        }


    }


}
