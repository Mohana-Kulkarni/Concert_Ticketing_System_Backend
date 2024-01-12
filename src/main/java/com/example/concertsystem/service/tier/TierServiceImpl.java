package com.example.concertsystem.service.tier;

import com.example.concertsystem.entity.Place;
import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.entity.Venue;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.faunadb.client.query.Language.*;
import static com.faunadb.client.query.Language.Value;

@Service
public class TierServiceImpl implements TierService{

    private FaunaClient faunaClient;

    @Autowired
    public TierServiceImpl(FaunaClient faunaClient) {
        this.faunaClient= faunaClient;
    }
    @Override
    public void addTier(String name, int capacity, int price) {
        faunaClient.query(
                Create(
                        Collection("Tier"),
                        Obj(
                                "data",
                                Obj(
                                        "name", Value(name),
                                        "capacity", Value(capacity),
                                        "price", Value(price)
                                )
                        )
                )
        );
    }

    @Override
    public Tier getTierById(String id) throws ExecutionException, InterruptedException {
        Value res = faunaClient.query(Get(Ref(Collection("Tier"), id))).get();

        return new Tier(
                res.at("ref").to(Value.RefV.class).get().getId(),
                res.at("data", "name").to(String.class).get(),
                res.at("data", "capacity").to(Integer.class).get(),
                res.at("data","price").to(Integer.class).get()
        );
    }

    @Override
    public Tier getTierByName(String name) throws ExecutionException, InterruptedException {
        CompletableFuture<Value> res = faunaClient.query(Get(Match(Index("tier_by_name"), Value(name))));
        return new Tier(
                res.get().at("ref").to(Value.RefV.class).get().getId(),
                res.get().at("data", "name").to(String.class).get(),
                res.get().at("data", "capacity").to(Integer.class).get(),
                res.get().at("data", "price").to(Integer.class).get()
        );
    }

    @Override
    public void updateTier(String id, String name, int capacity, int price) throws ExecutionException, InterruptedException {
        faunaClient.query(
                Update(Ref(Collection("Tier"), id),
                        Obj(
                                "data", Obj(
                                        "name", Value(name),
                                        "capacity", Value(capacity),
                                        "price", Value(price)
                                )
                        )
                )
        ).get();
    }

    @Override
    public void deleteTierById(String id) {
        faunaClient.query(Delete(Ref(Collection("Tier"), id)));
    }
}
