package com.example.concertsystem.service.user;

import com.example.concertsystem.dto.UserResponse;
import com.example.concertsystem.exception.classes.UserNotFoundException;
import com.example.concertsystem.service.firebase.FirebaseService;
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
    public void addUser(String name, String userName, String userEmail, String profileImg, String walletId, String transactionId) throws IOException {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("userName", userName);
        userData.put("userEmail" , userEmail);
        userData.put("walletId", walletId);
        userData.put("transactionId", transactionId);
        userData.put("profileImg", profileImg);
        faunaClient.query(
                Create(
                        Collection("User"),
                        Obj(
                                "data",
                                Value(userData)
                        )
                )
        );
    }

    @Override
    public UserResponse isUserRegistered(String walletId) throws ExecutionException, InterruptedException {
        try {
            Value val = faunaClient.query(
                    Get(
                            Match(Index("user_by_walletId"), Value(walletId))
                    )
            ).get();
            return new UserResponse(
                    val.at("ref").to(Value.RefV.class).get().getId(),
                    val.at("data", "name").to(String.class).get(),
                    val.at("data", "userName").to(String.class).get(),
                    val.at("data", "userEmail").to(String.class).get(),
                    val.at("data","walletId").to(String.class).get(),
                    val.at("data", "transactionId").to(String.class).get(),
                    val.at("data", "profileImg").to(String.class).get()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public UserResponse getUserById(String id) throws UserNotFoundException{
        try {
            Value res = faunaClient.query(Get(Ref(Collection("User"), id))).get();

            return new UserResponse(
                    res.at("ref").to(Value.RefV.class).get().getId(),
                    res.at("data", "name").to(String.class).get(),
                    res.at("data", "userName").to(String.class).get(),
                    res.at("data", "userEmail").to(String.class).get(),
                    res.at("data", "walletId").to(String.class).get(),
                    res.at("data", "transactionId").to(String.class).get(),
                    res.at("data", "profileImg").to(String.class).get()
            );
        } catch (Exception e) {
            throw new UserNotFoundException("User id not found - " + id);
        }
    }

    @Override
    public List<UserResponse> getUsersByType(String role) {
        CompletableFuture<Value> result = faunaClient.query(
                Map(
                        Paginate(Match(Index("users_by_type"), Value(role))),
                        Lambda("ref", Get(Var("ref")))
                )

        );

        return parseUserResult(result);
    }

    @Override
    public void updateUserInfo(String id, String name, String userName, String userEmail, String profileImg, String walletId, String transactionId) throws ExecutionException, InterruptedException, IOException {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("userName", userName);
        userData.put("userEmail" , userEmail);
        userData.put("walletId", walletId);
        userData.put("transactionId", transactionId);
        userData.put("profileImg", profileImg);
        faunaClient.query(
                Update(Ref(Collection("User"), id),
                        Obj(
                                "data",
                                Value(userData)
                        )
                )
        ).get();
    }

    @Override
    public List<UserResponse> getUserListById(List<String> userId) throws ExecutionException, InterruptedException {
        List<UserResponse> userList = new ArrayList<>();
        for(String id : userId) {
            UserResponse user = getUserById(id);
            userList.add(user);
        }
        return userList;
    }


    public void updateUserRole(String id, String name, String role) throws ExecutionException, InterruptedException {
        try {
            faunaClient.query(
                    Update(Ref(Collection("User"), id),
                            Obj(
                                    "data", Obj(
                                            "name", Value(name),
                                            "type", Value(role)
                                    )
                            )
                    )
            ).get();
        } catch (Exception e) {
            throw new UserNotFoundException("User id not found - " + id);
        }
    }

    @Override
    public void deleteUser(String id) throws UserNotFoundException {
       try {
           faunaClient.query(Delete(Ref(Collection("User"), id)));

       } catch (Exception e) {
           throw new UserNotFoundException("User id not found - " + id);
       }
    }


    public List<String> getUserIdsByUserName(List<String> userName){
        List<String> userRefs = new ArrayList<>();
        for(String user : userName){
            String value = getIdByUserName(user);
            userRefs.add(value);
        }
        return userRefs;


    }
    public String getIdByUserName(String userName){
        String value = faunaClient.query(Get(Match(Index("user_by_username"),
                Value(userName)))).join().at("ref").get(Value.RefV.class).getId();


        return value;


    }

    private List<UserResponse> parseUserResult(CompletableFuture<Value> result) {
        try {
            Value res = result.join();
            List<Value> userData = res.at("data").to(List.class).get();
            System.out.println(userData.size());

            List<UserResponse> userList = new ArrayList<>();
            for (Value userValue : userData) {
                Value.RefV ref = userValue.at("ref").get(Value.RefV.class);
                String id = ref.getId();
                String name = userValue.at("data", "name").get(String.class);
                String userName = userValue.at("data", "userName").to(String.class).get();
                String walletId = userValue.at("data", "walletId").to(String.class).get();
                String userEmail = userValue.at("data", "userEmail").to(String.class).get();
                String profileImg =userValue.at("data", "profileImg").to(String.class).get();
                String transactionId = userValue.at("data", "transactionId").to(String.class).get();

                UserResponse user = new UserResponse(id, name, userName, userEmail, walletId, transactionId, profileImg);
                userList.add(user);

            }
            return userList;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

    }

}
