package com.example.concertsystem.service.organiser;

import com.example.concertsystem.entity.Organiser;
import com.example.concertsystem.exception.classes.OrganiserNotFoundException;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.query.Expr;
import com.faunadb.client.types.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
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
    public void addOrganiser(String name, String userName, String email, String govId, String walletId, String transactionId) {
        Map<String, Object> organiserData = new HashMap<>();
        organiserData.put("name", name);
        organiserData.put("userName", userName);
        organiserData.put("email" , email);
        organiserData.put("govId", govId);
        organiserData.put("walletId", walletId);
        organiserData.put("transactionId", transactionId);
        faunaClient.query(
                Create(
                        Collection("Organiser"),
                        Obj(
                                "data", Value(organiserData)
                        )
                )
        );
    }
    @Override
    public Organiser getOrganiserById(String id) throws ExecutionException, InterruptedException {
        try {
            Value res = faunaClient.query(Get(Ref(Collection("Organiser"), id))).get();

            return new Organiser(
                    res.at("ref").to(Value.RefV.class).get().getId(),
                    res.at("data", "name").to(String.class).get(),
                    res.at("data", "userName").to(String.class).get(),
                    res.at("data", "email").to(String.class).get(),
                    res.at("data", "govId").to(String.class).get(),
                    res.at("data", "walletId").to(String.class).get(),
                    res.at("data", "transactionId").to(String.class).get()
            );
        } catch (Exception e) {
            throw new OrganiserNotFoundException("Organiser id not found - " + id);
        }
    }

    @Override
    public void updateOrganiser(String id, String name, String userName, String email, String govId, String walletId, String transactionId) throws ExecutionException, InterruptedException {
        try {
            Map<String, Object> organiserData = new HashMap<>();
            organiserData.put("name", name);
            organiserData.put("userName", userName);
            organiserData.put("email" , email);
            organiserData.put("govId", govId);
            organiserData.put("walletId", walletId);
            organiserData.put("transactionId", transactionId);
            faunaClient.query(
                    Update(Ref(Collection("Organiser"), id),
                            Obj(
                                    "data", Value(organiserData)
                            )
                    )
            ).get();
        } catch (Exception e) {
            throw new OrganiserNotFoundException("Organiser id not found - " + id);
        }
    }

    @Override
    public void deleteOrganiser(String id) {
        try {
            faunaClient.query(Delete(Ref(Collection("Organiser"), id)));
        } catch (Exception e) {
            throw new OrganiserNotFoundException("Organiser id not found - " + id);
        }
    }
}
