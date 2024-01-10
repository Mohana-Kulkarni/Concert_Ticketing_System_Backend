package com.example.concertsystem.service.place;

import com.example.concertsystem.entity.Place;
import com.example.concertsystem.entity.User;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Value res = faunaClient.query(Get(Ref(Collection("Place"), id))).get();

        return new Place(
                res.at("ref").to(Value.RefV.class).get().getId(),
                res.at("data", "name").to(String.class).get()
        );
    }

    @Override
    public Place getPlaceByName(String name) throws ExecutionException, InterruptedException {
        CompletableFuture<Value> res = faunaClient.query(Get(Match(Index("place_by_name"), Value(name))));
        return new Place(
                res.get().at("ref").to(Value.RefV.class).get().getId(),
                res.get().at("data", "name").to(String.class).get()
        );
    }

    @Override
    public void updatePlaceById(String id, String name) throws ExecutionException, InterruptedException {
        faunaClient.query(
                Update(Ref(Collection("Place"), id),
                        Obj(
                                "data", Obj(
                                        "name", Value(name))
                        )
                )
        ).get();
    }

    @Override
    public void deletePlaceById(String id) {
        faunaClient.query(Delete(Ref(Collection("Place"), id)));
    }
}
