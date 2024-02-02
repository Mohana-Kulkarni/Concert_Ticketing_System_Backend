package com.example.concertsystem.service.venue;

import com.example.concertsystem.entity.Event;
import com.example.concertsystem.entity.Venue;
import com.example.concertsystem.exception.classes.VenueNotFoundException;
import com.example.concertsystem.service.place.PlaceService;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.errors.NotFoundException;
import com.faunadb.client.query.Language;
import com.faunadb.client.types.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
    private PlaceService placeService;

    @Autowired
    public VenueServiceImpl(FaunaClient faunaClient,PlaceService placeService) {
        this.faunaClient= faunaClient;
        this.placeService = placeService;
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
    public Venue getVenueById(String id) throws VenueNotFoundException {
        try {
            Value res = faunaClient.query(Get(Ref(Collection("Venue"), id))).get();

            return new Venue(
                    res.at("ref").to(Value.RefV.class).get().getId(),
                    res.at("data", "name").to(String.class).get(),
                    res.at("data", "address").to(String.class).get(),
                    res.at("data", "capacity").to(Integer.class).get(),
                    res.at("data","placeId").to(String.class).get()
            );
        } catch (Exception e) {
            throw new VenueNotFoundException("Venue id not found - " + id);
        }
    }

    @Override
    public Venue getVenueByName(String name) throws ExecutionException, InterruptedException {
        try {
            Value res = faunaClient.query(Get(Match(Index("venues_by_name"), Value(name)))).join();
            return new Venue(
                    res.at("ref").to(Value.RefV.class).get().getId(),
                    res.at("data", "name").to(String.class).get(),
                    res.at("data", "address").to(String.class).get(),
                    res.at("data", "capacity").to(Integer.class).get(),
                    String.valueOf(res.at("data", "placeId").to(String.class).get())
            );
        }catch (Exception e) {
            throw new VenueNotFoundException("Venue name not found - " + name);
        }
    }

    @Override
    public List<Venue> getVenueByPlace(String place) throws ExecutionException, InterruptedException {
        try {
            List<String> venueIds = getVenueIdsByPlaceName(place);
            List<Venue> venues = new ArrayList<>();
            for(String id : venueIds){
                Venue venue = getVenueById(id);
                venues.add(venue);
            }
            return venues;
        } catch (Exception e) {
            throw new VenueNotFoundException("Venue with place not found - " + place);
        }
    }


    public List<String> getVenueIdsByPlaceName(String placeName) throws ExecutionException, InterruptedException {
        String placeRef = placeService.getPlaceIdByPlaceName(placeName);
        ArrayList<Value> res = faunaClient.query(
                Paginate(Match(Index("venue_by_placeId"), Value(placeRef)))
        ).get().at("data").get(ArrayList.class);
        List<String> venueIds = new ArrayList<>();
        for(int i=0;i<res.size();i++){
            String venueId = res.get(i).get(Value.RefV.class).getId();
            venueIds.add(venueId);
        }
        return venueIds;
    }



    public String getVenueIdByVenueName(String venueName){
        String value = faunaClient.query(Get(Match(Index("venues_by_name"),
                Value(venueName)))).join().at("ref").get(Value.RefV.class).getId();
        return value;
    }


    @Override
    public void updateVenueById(String id, String name, String address, int capacity, String placeId) throws ExecutionException, InterruptedException {

        try {
            faunaClient.query(
                    Update(Ref(Collection("Venue"), id),
                            Obj(
                                    "data", Obj(
                                            "name", Value(name),
                                            "address", Value(address),
                                            "capacity", Value(capacity),
                                            "placeId", Value(placeId)
                                    )
                            )
                    )
            ).get();
        } catch (Exception e) {
            throw new VenueNotFoundException("Venue id not found - " + id);
        }
    }

    @Override
    public void deleteVenueById(String id) {
        try {
            faunaClient.query(Delete(Ref(Collection("Venue"), id)));
        } catch (Exception e) {
            throw new VenueNotFoundException("Venue id not found - " + id);
        }
    }
}
