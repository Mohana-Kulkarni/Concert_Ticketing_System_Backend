package com.example.concertsystem.service.organiser;

import com.example.concertsystem.entity.Organiser;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static com.faunadb.client.query.Language.*;
import static com.faunadb.client.query.Language.Value;

@Service
public class OrganiserServiceImpl implements OrganiserService{
    private FaunaClient faunaClient;

    public OrganiserServiceImpl(FaunaClient faunaClient) {
        this.faunaClient= faunaClient;
    }
    @Override
    public void addOrganiser(String name, String userName, String email, String govId, String walletId) {
        faunaClient.query(
                Create(
                        Collection("Organiser"),
                        Obj(
                                "data",
                                Obj(
                                        "name", Value(name),
                                        "userName", Value(userName),
                                        "email", Value(email),
                                        "govId", Value(govId),
                                        "walletId", Value(walletId)
                                )
                        )
                )
        );
    }

    @Override
    public Organiser getOrganiserById(String id) throws ExecutionException, InterruptedException {
        Value res = faunaClient.query(Get(Ref(Collection("Organiser"), id))).get();

        return new Organiser(
                res.at("ref").to(Value.RefV.class).get().getId(),
                res.at("data", "name").to(String.class).get(),
                res.at("data", "userName").to(String.class).get(),
                res.at("data", "email").to(String.class).get(),
                res.at("data", "govId").to(String.class).get(),
                res.at("data", "walletId").to(String.class).get()
                );
    }

    @Override
    public void updateOrganiser(String id, String name, String userName, String email, String govId, String walletId) throws ExecutionException, InterruptedException {
        faunaClient.query(
                Update(Ref(Collection("Organiser"), id),
                        Obj(
                                "data", Obj(
                                        "name", Value(name),
                                        "userName", Value(userName),
                                        "email", Value(email),
                                        "govId", Value(govId),
                                        "walletId", Value(walletId)
                                )
                        )
                )
        ).get();
    }

    @Override
    public void deleteOrganiser(String id) {
        faunaClient.query(Delete(Ref(Collection("Organiser"), id)));
    }
}
