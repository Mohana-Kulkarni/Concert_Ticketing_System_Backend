package com.example.concertsystem.service.user;

import com.example.concertsystem.dto.UserResponse;
import com.example.concertsystem.service.firebase.FirebaseService;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.beans.factory.annotation.Autowired;

import static com.faunadb.client.query.Language.*;
import static com.faunadb.client.query.Language.Value;

import com.example.concertsystem.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class UserServiceImpl implements UserService{
    private FaunaClient faunaClient;
    private FirebaseService firebaseService;

    @Autowired
    public UserServiceImpl(FaunaClient faunaClient, FirebaseService firebaseService) {
        this.faunaClient= faunaClient;
        this.firebaseService = firebaseService;
    }
    @Override
    public void addUser(String name, String userName, String walletId, String userEmail, MultipartFile profileImg) throws IOException {
        String profile = firebaseService.upload(profileImg);
        faunaClient.query(
                Create(
                        Collection("User"),
                        Obj(
                                "data",
                                Obj(
                                        "name", Value(name),
                                        "userName", Value(userName),
                                        "walletId", Value(walletId),
                                        "userEmail", Value(userEmail),
                                        "profileImg",Value(profile)
                                )
                        )
                )
        );
    }

    @Override
    public UserResponse getUserById(String id) throws ExecutionException, InterruptedException {
       Value res = faunaClient.query(Get(Ref(Collection("User"), id))).get();

       return new UserResponse(
               res.at("ref").to(Value.RefV.class).get().getId(),
               res.at("data", "name").to(String.class).get(),
               res.at("data", "userName").to(String.class).get(),
               res.at("data","walletId").to(String.class).get(),
               res.at("data", "userEmail").to(String.class).get(),
               res.at("data", "profileImg").to(String.class).get()
       );
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
    public void updateUserInfo(String id,String name, String userName, String walletId, String userEmail, MultipartFile profileImg) throws ExecutionException, InterruptedException, IOException {
        String profile = firebaseService.upload(profileImg);
        faunaClient.query(
                Update(Ref(Collection("User"), id),
                        Obj(
                                "data", Obj(
                                        "name", Value(name),
                                        "userName", Value(userName),
                                        "walletId", Value(walletId),
                                        "userEmail", Value(userEmail),
                                        "profileImg",Value(profileImg)
                                )
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
    }

    @Override
    public void deleteUser(String id) {
        faunaClient.query(Delete(Ref(Collection("User"), id)));
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

                UserResponse user = new UserResponse(id, name, userName, walletId, userEmail, profileImg);
                userList.add(user);

            }
            return userList;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

    }

}
