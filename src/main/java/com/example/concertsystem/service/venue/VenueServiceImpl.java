package com.example.concertsystem.service.venue;

import com.example.concertsystem.entity.Venue;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public void addVenue(Venue venue) {
        Value.RefV placeRef = getPlaceRef(venue.placeId());
        faunaClient.query(
                Create(
                        Collection("Venue"),
                        Obj(
                                "data",
                                Obj(
                                        "name", Value(venue.name()),
                                        "address", Value(venue.address()),
                                        "capacity", Value(venue.capacity()),
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
    public Venue getVenueByName(String name) throws ExecutionException, InterruptedException {
        CompletableFuture<Value> res = faunaClient.query(Get(Match(Index("venues_by_name"), Value(name))));
        return new Venue(
                res.get().at("ref").to(Value.RefV.class).get().getId(),
                res.get().at("data", "name").to(String.class).get(),
                res.get().at("data", "address").to(String.class).get(),
                res.get().at("data", "capacity").to(Integer.class).get(),
                res.get().at("data", "placeId").to(Value.RefV.class).get().getId()
        );
    }


//    @Override
//    public List<Venue> getVenuesByPlace(String place) throws ExecutionException, InterruptedException {
//        Value.RefV placeRef = getPlaceRefByName(place);
//        System.out.println(placeRef.getId());
//        CompletableFuture<List<Value>> result = faunaClient.query(
//                Paginate(
//                                Index("venues_by_place_ref")
//                ), Value(placeRef)
//        );
//
//        return parseVenueResult(result);
////        return null;
//    }
////
//    private Value.RefV getPlaceRefByName(String place) throws ExecutionException, InterruptedException {
//        return (Value.RefV) faunaClient.query(Match(Index("places_by_name"), Value(place))).get().at("ref");
//    }
//
//    private List<Venue> parseVenueResult(CompletableFuture<List<Value>> result) {
//        try {
//            Value res = (Value) result.join();
//            List<Value> venueData = res.at("data").to(List.class).get();
//            System.out.println(res.at("ref").to(String.class).get());
//            List<Venue> venueList = new ArrayList<>();
//            for (Value venueValue : venueData) {
//                String id = venueValue.at("ref").to(Value.RefV.class).get().getId();
//                String name = venueValue.at("data", "name").get(String.class);
//                String address = venueValue.at("data", "address").get(String.class);
//                int capacity = venueValue.at("data", "capacity").get(Integer.class);
//                String placeId = venueValue.at("data", "placeId").to(Value.RefV.class).get().getId();
//
//                Venue venue = new Venue(id, name, address, capacity, placeId);
//                venueList.add(venue);
//            }
//            return venueList;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Collections.emptyList();
//        }
//    }
    @Override
    public List<Venue> getVenuesByPlace(String place) throws ExecutionException, InterruptedException {
//        Value.RefV placeRef = getPlaceRefByName(place);
//        System.out.println(placeRef.getId());
        CompletableFuture<Value> result = faunaClient.query(
                Paginate(
                    Join(
                            Match(Index("place_by_name"), Value(place)),
                            Index("venues_by_place_ref")
                    )
            )
        );

        return parseVenueResult(result);
    }

    private Value.RefV getPlaceRefByName(String place) throws ExecutionException, InterruptedException {
        return faunaClient.query(Match(Index("places_by_name"), Value(place)))
                .get().at("ref").to(Value.RefV.class).get();
    }

    private List<Venue> parseVenueResult(CompletableFuture<Value> result) {
        try {
            Value res = result.join();
            List<Venue> venueData = res.at("data").to(List.class).get();
//            System.out.println(res.at("ref").to(String.class).get());
//            int i = 0;
//            List<Venue> venueList = new ArrayList<>();
//            for (Value venueValue : venueData) {
//                String id = venueValue.at("data", "id").to(Value.RefV.class).get().getId();
//                String name = venueValue.at("data", "name").get(String.class);
//                String address = venueValue.at("data", "address").get(String.class);
//                int capacity = venueValue.at("data", "capacity").get(Integer.class);
//                String placeId = venueValue.at("data", "placeId").to(Value.RefV.class).get().getId();

//                Venue venue = new Venue(id, name, address, capacity, placeId);
//                venueList.add(venue);
//            }
            return venueData;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    @Override
    public void updateVenueById(String id, Venue venue) throws ExecutionException, InterruptedException {

        faunaClient.query(
                Update(Ref(Collection("Venue"), id),
                        Obj(
                                "data", Obj(
                                        "name", Value(venue.name()),
                                        "address", Value(venue.address()),
                                        "capacity", Value(venue.capacity()),
                                        "placeId", Value(venue.placeId())
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
