package com.example.concertsystem.service.user;

import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.beans.factory.annotation.Autowired;

import static com.faunadb.client.query.Language.*;
import static com.faunadb.client.query.Language.Value;

import com.example.concertsystem.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    public void addUser(String name, String role) {
        faunaClient.query(
                Create(
                        Collection("User"),
                        Obj(
                                "data",
                                Obj(
                                        "name", Value(name),
                                        "type", Value(role))
                                )
                        )
                );
    }

    @Override
    public User getUserById(String id) throws ExecutionException, InterruptedException {
       Value res = faunaClient.query(Get(Ref(Collection("User"), id))).get();

       return new User(
               res.at("ref").to(Value.RefV.class).get().getId(),
               res.at("data", "name").to(String.class).get(),
               res.at("data", "type").to(String.class).get()
       );
    }

    @Override
    public List<User> getUsersByType(String role) {
        CompletableFuture<Value> result = faunaClient.query(
                Map(
                        Paginate(Match(Index("users_by_type"), Value(role))),
                        Lambda("ref", Get(Var("ref")))
                )

        );

        return parseUserResult(result);
    }

    private List<User> parseUserResult(CompletableFuture<Value> result) {
        try {
            Value res = result.join();
            List<Value> userData = res.at("data").to(List.class).get();
            System.out.println(userData.size());

            List<User> userList = new ArrayList<>();
            for (Value userValue : userData) {
                Value.RefV ref = userValue.at("ref").get(Value.RefV.class);
                String id = ref.getId();
                String name = userValue.at("data", "name").get(String.class);
                String type = userValue.at("data", "type").get(String.class);

                User user = new User(id, name, type);
                userList.add(user);

            }
            return userList;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

    }

    @Override
    public void updateUserInfo(String id, String name, String role) throws ExecutionException, InterruptedException {
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
}
