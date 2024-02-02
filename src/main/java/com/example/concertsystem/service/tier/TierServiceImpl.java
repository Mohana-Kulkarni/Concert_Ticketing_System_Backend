package com.example.concertsystem.service.tier;

import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.exception.classes.TierNotFoundException;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    public String addTier(String name, int capacity, int price) throws ExecutionException, InterruptedException {
        CompletableFuture<Value> res = faunaClient.query(
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
        Value value = res.join();
        return value.at("ref").to(Value.RefV.class).get().getId();
    }

    @Override
    public List<String> addNewTiers(List<Tier> tierList) throws ExecutionException, InterruptedException {
        List<String> tierIds = new ArrayList<>();
        for (Tier tier : tierList) {
           String tierId = addTier(tier.name(), tier.capacity(), tier.price());
           System.out.println(tierId);
           tierIds.add(tierId);
        }
        return tierIds;
    }

    @Override
    public Tier getTierById(String id) throws ExecutionException, InterruptedException {
        try {
            Value res = faunaClient.query(Get(Ref(Collection("Tier"), id))).get();

            return new Tier(
                    res.at("ref").to(Value.RefV.class).get().getId(),
                    res.at("data", "name").to(String.class).get(),
                    res.at("data", "capacity").to(Integer.class).get(),
                    res.at("data","price").to(Integer.class).get()
            );
        } catch (Exception e) {
            throw new TierNotFoundException("Tier id not found - " + id);
        }
    }

    @Override
    public List<String> getIdByTierName(List<String> tierName){
        int i = 0;
        try {
            List<String> tierRefs = new ArrayList<>();
            for(String tier : tierName){
                String value = getTierIdByTierName(tier);
                i++;
                tierRefs.add(value);
            }
            return tierRefs;
        } catch (Exception e) {
            throw new TierNotFoundException("Tier with name not found - " + tierName.get(i));
        }
    }


    @Override
    public String getTierIdByTierName(String tierName){
        try {
            return faunaClient.query(Get(Match(Index("tier_by_name"),
                    Value(tierName)))).join().at("ref").get(Value.RefV.class).getId();
        } catch (Exception e) {
            throw new TierNotFoundException("Tier with name not found - " + tierName);
        }

    }

    @Override
    public List<Tier> getTierListByIds(List<String> tierId) throws ExecutionException, InterruptedException {
        int i = 0;
        try {
            List<Tier> tierList = new ArrayList<>();
            for (String id : tierId) {
                Tier tier = getTierById(id);
                tierList.add(tier);
            }
            return tierList;
        } catch (Exception e) {
            throw new TierNotFoundException("Tier id not found - " + tierId);
        }
    }

    @Override
    public Tier getTierByName(String name) throws ExecutionException, InterruptedException {
        try {
            CompletableFuture<Value> res = faunaClient.query(Get(Match(Index("tier_by_name"), Value(name))));
            return new Tier(
                    res.get().at("ref").to(Value.RefV.class).get().getId(),
                    res.get().at("data", "name").to(String.class).get(),
                    res.get().at("data", "capacity").to(Integer.class).get(),
                    res.get().at("data", "price").to(Integer.class).get()
            );
        } catch (Exception e) {
            throw new TierNotFoundException("Tier with name not found - " + name);
        }
    }

    @Override
    public void updateTier(String id, String name, int capacity, int price) throws ExecutionException, InterruptedException {
        try {
            Value res = faunaClient.query(
                    Update(
                            Ref(Collection("Tier"), id),
                            Obj(
                                    "data", Obj(
                                            "name", Value(name),
                                            "capacity", Value(capacity),
                                            "price", Value(price)
                                    )
                            )
                    )
            ).get();
        } catch (Exception e) {
            throw new TierNotFoundException("Tier id not found - " + id);
        }

    }

    @Override
    public void deleteTierById(String id) {
        try {
            faunaClient.query(Delete(Ref(Collection("Tier"), id)));
        } catch (Exception e) {
            throw new TierNotFoundException("Tier id not found - " + id);
        }
    }
}
