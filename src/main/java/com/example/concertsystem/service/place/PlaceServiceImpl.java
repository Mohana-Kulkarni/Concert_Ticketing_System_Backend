package com.example.concertsystem.service.place;

import com.example.concertsystem.entity.Place;
import com.example.concertsystem.exception_handling.classes.PlaceNotFoundException;
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
public class PlaceServiceImpl implements PlaceService{
    private FaunaClient faunaClient;

    @Autowired
    public PlaceServiceImpl(FaunaClient faunaClient) {
        this.faunaClient= faunaClient;
    }
    @Override
    public void addPlace(String name) {
        faunaClient.query(
                Create(
                        Collection("Place"),
                        Obj(
                                "data",
                                Obj(
                                        "name", Value(name)
                                )
                        )
                )
        );
    }

    @Override
    public Place getPlaceById(String id) throws ExecutionException, InterruptedException {
        try {
            Value res = faunaClient.query(Get(Ref(Collection("Place"), id))).get();

            return new Place(
                    res.at("ref").to(Value.RefV.class).get().getId(),
                    res.at("data", "name").to(String.class).get()
            );
        } catch (Exception e) {
            throw new PlaceNotFoundException("Place id not found - " + id);
        }
    }

    @Override
    public Place getPlaceByName(String name) throws ExecutionException, InterruptedException {
       try {
           CompletableFuture<Value> res = faunaClient.query(Get(Match(Index("place_by_name"), Value(name))));
           return new Place(
                   res.get().at("ref").to(Value.RefV.class).get().getId(),
                   res.get().at("data", "name").to(String.class).get()
           );
       } catch (Exception e) {
           throw new PlaceNotFoundException("Place name not found - " + name);
       }
    }

    @Override
    public List<Place> getAllPlaces() throws ExecutionException, InterruptedException {
        try {
            List<Value> res = (List<Value>) faunaClient.query(
                    Map(
                            Paginate(Documents(Collection("Place"))),
                            Lambda("placeRef", Get(Var("placeRef")))
                    )
            ).get();

            List<Place> places = new ArrayList<>();
            for(Value val : res) {
                Place place = new Place(
                        val.at("ref").to(Value.RefV.class).get().getId(),
                        val.at("data", "city").to(String.class).get()
                );
                places.add(place);
            }
            return places;
        } catch (Exception e) {
            throw new PlaceNotFoundException("Place collection is empty!!");
        }
    }

    @Override
    public void updatePlaceById(String id, String name) throws ExecutionException, InterruptedException {
        try {
            faunaClient.query(
                    Update(Ref(Collection("Place"), id),
                            Obj(
                                    "data", Obj(
                                            "name", Value(name))
                            )
                    )
            ).get();
        } catch (Exception e) {
            throw new PlaceNotFoundException("Place id not found - " + id);
        }
    }


    @Override
    public void deletePlaceById(String id) {
        try{
            faunaClient.query(Delete(Ref(Collection("Place"), id)));
        } catch (Exception e) {
            throw new PlaceNotFoundException("Place id not found - " + id);
        }
    }

    @Override
    public String getPlaceIdByPlaceName(String placeName) {
        try {
            return faunaClient.query(Get(Match(Index("place_by_placeName"),
                    Value(placeName)))).join().at("ref").get(Value.RefV.class).getId();
        } catch (Exception e) {
            throw new PlaceNotFoundException("Place name not found - " + placeName);
        }

    }
}
