package com.example.concertsystem.service.venue;

import com.example.concertsystem.entity.Venue;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.faunadb.client.query.Language.*;
import static com.faunadb.client.query.Language.Obj;

@Service
public class VenueServiceImpl implements VenueService{
    private FaunaClient faunaClient;

    @Autowired
    public VenueServiceImpl(FaunaClient faunaClient) {
        this.faunaClient= faunaClient;
    }
    @Override
    public void addVenue(String name, String address, int capacity, String placeId) {
        Value.RefV placeRef = getPlaceRef(placeId);
        faunaClient.query(
                Create(
                        Collection("Venue"),
                        Obj(
                                "data",
                                Obj(
                                        "name", Value(name),
                                        "address", Value(address),
                                        "capacity", Value(capacity),
                                        "placeId", placeRef
                                )
                        )
                )
        );
    }

    private Value.RefV getPlaceRef(String placeId) {
        CompletableFuture<Value> result = faunaClient.query(Get(Ref(Collection("Place"), placeId)));
        try {
            Value res = result.join();
            Value.RefV documentId = res.at("ref").to(Value.RefV.class).get();
            System.out.println("The ref is : "  + documentId);
            return documentId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Venue getVenueById(String id) throws ExecutionException, InterruptedException {
        Value res = faunaClient.query(Get(Ref(Collection("Venue"), id))).get();

        return new Venue(
                res.at("ref").to(Value.RefV.class).get().getId(),
                res.at("data", "name").to(String.class).get(),
                res.at("data", "address").to(String.class).get(),
                res.at("data", "capacity").to(Integer.class).get(),
                res.at("data","placeId").to(Value.RefV.class).get().getId()
        );
    }


    @Override
    public List<Venue> getVenuesByPlace(String place) throws ExecutionException, InterruptedException {
        Value.RefV placeRef = faunaClient.query(
                 Get(Match(Index("places_by_name"), Value(place)))
        ).get().at("ref").get(Value.RefV.class);
        System.out.println(placeRef.getId());
        CompletableFuture<Value> result = faunaClient.query(
                Paginate(
                            Match(Index("venues_by_place_ref"), placeRef)
                )
        );

        return parseVenueResult(result);
//        return null;
    }
//
//    private Value getPlaceRefByName(String place) throws ExecutionException, InterruptedException {
//        return faunaClient.query(Get(Match(Index("places_by_name"), Value(place)))).get();
//    }

    private List<Venue> parseVenueResult(CompletableFuture<Value> result) {
        try {
            Value res = result.join();
            return res.at("data").to(List.class).get();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public void updateVenueById(String id, String name, String address, int capacity, String placeId) throws ExecutionException, InterruptedException {
        Value.RefV placeRef = getPlaceRef(placeId);
        faunaClient.query(
                Update(Ref(Collection("Venue"), id),
                        Obj(
                                "data", Obj(
                                        "name", Value(name),
                                        "address", Value(address),
                                        "capacity", Value(capacity),
                                        "placeId", placeRef
                                )
                        )
                )
        ).get();
    }

    @Override
    public void deleteVenueById(String id) {
        faunaClient.query(Delete(Ref(Collection("Venue"), id)));
    }
}
