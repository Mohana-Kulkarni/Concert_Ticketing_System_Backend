package com.example.concertsystem.service.place;

import com.example.concertsystem.constants.GlobalConstants;
import com.example.concertsystem.dto.PlaceResponse;
import com.example.concertsystem.entity.Place;
import com.example.concertsystem.exception.ResourceNotFoundException;
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
    public boolean addPlace(String name) {
        boolean popular = false;
        try{
        faunaClient.query(
                Create(
                        Collection("Place"),
                        Obj(
                                "data",
                                Obj(
                                        "name", Value(name),
                                        "popular", Value(popular),
                                        "eventCount", Value(0)
                                )
                        )
                )
        );
        return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public PlaceResponse getPlaceById(String id){
        try {
            Value res = faunaClient.query(Get(Ref(Collection("Place"), id))).get();

            return new PlaceResponse(
                    res.at("ref").to(Value.RefV.class).get().getId(),
                    res.at("data", "name").to(String.class).get(),
                    res.at("data", "popular").to(Boolean.class).get()
            );
        } catch (Exception e) {
            throw new ResourceNotFoundException("Place","PlaceId",id);
        }
    }

    @Override
    public PlaceResponse getPlaceByName(String name){
       try {
           Value res = faunaClient.query(Get(Match(Index("place_by_name"), Value(name)))).get();
           return new PlaceResponse(
                   res.at("ref").to(Value.RefV.class).get().getId(),
                   res.at("data", "name").to(String.class).get(),
                   res.at("data", "popular").to(Boolean.class).get()
           );
       } catch (Exception e) {
           throw new ResourceNotFoundException("Place","PlaceName",name);
       }
    }

    @Override
    public void updateEventCount(String name) throws ExecutionException, InterruptedException {
        Value res = faunaClient.query(Get(Match(Index("place_by_name"), Value(name)))).get();
        String id = res.at("ref").to(Value.RefV.class).get().getId();
        int count = res.at("data", "eventCount").to(Integer.class).get();
        updatePlaceById(id, name, ++count);
    }

    @Override
    public List<PlaceResponse> getAllPlaces(){
        try {
            Value value = faunaClient.query(
                    Paginate(Documents(Collection("Place")))
            ).get();
            List<Value> res = value.at("data").collect(Value.class).stream().toList();

            List<PlaceResponse> places = new ArrayList<>();
            for(Value val : res) {
                String id = val.get(Value.RefV.class).getId();
                PlaceResponse place = getPlaceById(id);
                places.add(place);
            }
            return places;
        } catch (Exception e) {
            throw new RuntimeException(GlobalConstants.MESSAGE_500);
        }
    }

    @Override
    public boolean updatePlaceById(String id, String name, int count){
        try {
            getPlaceById(id);
            boolean popular = false;
            if(count >= 2) {
                popular = true;
            }
            try {
                faunaClient.query(
                        Update(Ref(Collection("Place"), id),
                                Obj(
                                        "data", Obj(
                                                "name", Value(name),
                                                "popular", Value(popular),
                                                "eventCount", Value(count)
                                        )
                                )
                        )
                ).get();
                return true;
            } catch (Exception e) {
                return false;
            }
        }catch (Exception e){
            throw new ResourceNotFoundException("Place","Id",id);
        }
    }


    @Override
    public boolean deletePlaceById(String id) {
        try {
            getPlaceById(id);
            try {
                Value val = faunaClient.query(Delete(Ref(Collection("Place"), id))).get();
                if (val == null)
                    return false;
                return true;
            } catch (Exception e) {
                return false;
            }
        }catch (Exception e){
            throw new ResourceNotFoundException("Place","Id",id);
        }
    }

    @Override
    public String getPlaceIdByPlaceName(String placeName) {
        try {
            return faunaClient.query(Get(Match(Index("place_by_placeName"),
                    Value(placeName)))).join().at("ref").get(Value.RefV.class).getId();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Place","PlaceName",placeName);
        }

    }


}
