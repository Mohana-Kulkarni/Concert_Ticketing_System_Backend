package com.example.concertsystem.service.tier;

import com.example.concertsystem.entity.Place;
import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.entity.Venue;
import com.example.concertsystem.service.event.EventService;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.query.Expr;
import com.faunadb.client.types.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    public void addTier(String name, int capacity, int price, String eventId) throws ExecutionException, InterruptedException {
        faunaClient.query(
                Create(
                        Collection("Tier"),
                        Obj(
                                "data",
                                Obj(
                                        "name", Value(name),
                                        "capacity", Value(capacity),
                                        "price", Value(price),
                                        "eventId", Value(eventId)
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
                res.at("data","price").to(Integer.class).get(),
                res.at("data", "eventId").to(String.class).get()
        );
    }

    @Override
    public List<String> getIdByTierName(List<String> tierName){
        List<String> tierRefs = new ArrayList<>();
        for(String tier : tierName){
            String value = faunaClient.query(Get(Match(Index("tier_by_name"),
                    Value(tier)))).join().at("ref").get(Value.RefV.class).getId();
            tierRefs.add(value);
        }
        return tierRefs;
    }


    @Override
    public String getTierIdByTierName(String tierName){
        return faunaClient.query(Get(Match(Index("tier_by_name"),
                    Value(tierName)))).join().at("ref").get(Value.RefV.class).getId();

    }

    @Override
    public List<Tier> getTierListByIds(List<String> tierId) throws ExecutionException, InterruptedException {

        List<Tier> tierList = new ArrayList<>();
        for (String id : tierId) {
            Tier tier = getTierById(id);
            tierList.add(tier);
        }
        return tierList;
    }

    @Override
    public Tier getTierByNameEventId(String name, String eventId) throws ExecutionException, InterruptedException {
        CompletableFuture<Value> res = faunaClient.query(
                Map(
                        Paginate(
                                Match(Index("tier_by_name"), Value(name))
                        ), Lambda("tierRef", Get(Var("tierRef")))
                )
        );
        List<Value> value= res.join().at("data").to(List.class).get();
        System.out.println(value);
        Tier tier = null;
        for (Value val : value) {
            String event_id = val.at("data", "eventId").to(String.class).get();
            System.out.println(event_id);
            if(event_id.equals(eventId)) {
                return new Tier(
                    val.at("ref").to(Value.RefV.class).get().getId(),
                    val.at("data", "name").to(String.class).get(),
                    val.at("data", "capacity").to(Integer.class).get(),
                    val.at("data","price").to(Integer.class).get(),
                    val.at("data", "eventId").to(String.class).get()
                );
            }
        }
//        Value res = faunaClient.query(
//                Get(Match(Index("tier_by_tierName_eventId"), nam))
//        ).get();
//        return new Tier(
//                res.at("ref").to(Value.RefV.class).get().getId(),
//                res.at("data", "name").to(String.class).get(),
//                res.at("data", "capacity").to(Integer.class).get(),
//                res.at("data","price").to(Integer.class).get(),
//                res.at("data", "eventId").to(String.class).get()
//        );
        return tier;
    }


    @Override
    public Tier getTierByName(String name) throws ExecutionException, InterruptedException {
        CompletableFuture<Value> res = faunaClient.query(Get(Match(Index("tier_by_name"), Value(name))));
        return new Tier(
                res.get().at("ref").to(Value.RefV.class).get().getId(),
                res.get().at("data", "name").to(String.class).get(),
                res.get().at("data", "capacity").to(Integer.class).get(),
                res.get().at("data", "price").to(Integer.class).get(),
                res.get().at("data", "eventId").to(String.class).get()
        );
    }

    @Override
    public void updateTier(String id, String name, int capacity, int price, String eventId) throws ExecutionException, InterruptedException {
        Value res = faunaClient.query(
                Update(
                        Ref(Collection("Tier"), id),
                        Obj(
                                "data", Obj(
                                        "name", Value(name),
                                        "capacity", Value(capacity),
                                        "price", Value(price),
                                        "eventId", Value(eventId)
                                )
                        )
                )
        ).get();

        System.out.println(res);
    }

    @Override
    public void deleteTierById(String id) {
        faunaClient.query(Delete(Ref(Collection("Tier"), id)));
    }
}
