package com.example.concertsystem.service.tier;

import com.example.concertsystem.constants.GlobalConstants;
import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.exception.ResourceNotFoundException;
import com.example.concertsystem.exception_handling.classes.TierNotFoundException;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    public String addTier(String name, int capacity, int price){
        try {
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
        }catch(Exception e){
            throw new RuntimeException(GlobalConstants.MESSAGE_417_POST);
        }
    }

    @Override
    public List<String> addNewTiers(List<Tier> tierList){
        try {
            List<String> tierIds = new ArrayList<>();
            for (Tier tier : tierList) {
                String tierId = addTier(tier.name(), tier.capacity(), tier.price());
                tierIds.add(tierId);
            }
            return tierIds;
        }catch(Exception e){
            throw new RuntimeException(GlobalConstants.MESSAGE_500);
        }
    }

    @Override
    public Tier getTierById(String id){
        try {
            Value res = faunaClient.query(Get(Ref(Collection("Tier"), id))).get();

            return new Tier(
                    res.at("ref").to(Value.RefV.class).get().getId(),
                    res.at("data", "name").to(String.class).get(),
                    res.at("data", "capacity").to(Integer.class).get(),
                    res.at("data","price").to(Integer.class).get()
            );
        } catch (Exception e) {
            throw new ResourceNotFoundException("Tier","id",id);
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
            throw new ResourceNotFoundException("Tier","TierName",tierName.get(i));
        }
    }


    @Override
    public String getTierIdByTierName(String tierName){
        try {
            return faunaClient.query(Get(Match(Index("tier_by_name"),
                    Value(tierName)))).join().at("ref").get(Value.RefV.class).getId();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Tier","TierName",tierName);
        }

    }

    @Override
    public List<Tier> getTierListByIds(List<String> tierId){
        int i = 0;
        try {
            List<Tier> tierList = new ArrayList<>();
            for (String id : tierId) {
                Tier tier = getTierById(id);
                tierList.add(tier);
            }
            return tierList;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Tier","TierId",tierId.get(i));
        }
    }

    @Override
    public Tier getTierByName(String name){
        try {
            CompletableFuture<Value> res = faunaClient.query(Get(Match(Index("tier_by_name"), Value(name))));
            return new Tier(
                    res.get().at("ref").to(Value.RefV.class).get().getId(),
                    res.get().at("data", "name").to(String.class).get(),
                    res.get().at("data", "capacity").to(Integer.class).get(),
                    res.get().at("data", "price").to(Integer.class).get()
            );
        } catch (Exception e) {
            throw new ResourceNotFoundException("Tier","TierName",name);
        }
    }

    @Override
    public boolean updateTier(String id, String name, int capacity, int price){
        try {
            getTierById(id);
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
                return true;
            } catch (Exception e) {
                return false;
            }
        }catch (Exception e){
            throw new ResourceNotFoundException("Tier","Id",id);
        }

    }

    @Override
    public boolean deleteTierById(String id) {
        try {
            getTierById(id);
            try {
                Value val = faunaClient.query(Delete(Ref(Collection("Tier"), id))).get();
                if (val == null)
                    return false;
                return true;
            } catch (Exception e) {
                return false;
            }
        }catch (Exception e){
            throw new ResourceNotFoundException("Tier","Id",id);
        }
    }
}
