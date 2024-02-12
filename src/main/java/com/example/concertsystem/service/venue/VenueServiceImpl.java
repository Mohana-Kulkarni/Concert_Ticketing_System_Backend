package com.example.concertsystem.service.venue;

import com.example.concertsystem.entity.Venue;
import com.example.concertsystem.exception.ResourceNotFoundException;
import com.example.concertsystem.exception_handling.classes.VenueNotFoundException;
import com.example.concertsystem.service.place.PlaceService;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public boolean addVenue(Venue venue) {
        try {
            String placeRef = getPlaceRef(venue.placeId()).getId();
            faunaClient.query(
                    Create(
                            Collection("Venue"),
                            Obj(
                                    "data",
                                    Obj(
                                            "name", Value(venue.name()),
                                            "address", Value(venue.address()),
                                            "capacity", Value(venue.capacity()),
                                            "placeId", Value(placeRef)
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
            throw new ResourceNotFoundException("Venue","VenueId",id);
        }
    }

    @Override
    public Venue getVenueByName(String name){
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
            throw new ResourceNotFoundException("Venue","VenueName",name);
        }
    }

    @Override
    public List<Venue> getVenueByPlace(String place){
        try {
            List<String> venueIds = getVenueIdsByPlaceName(place);
            List<Venue> venues = new ArrayList<>();
            for(String id : venueIds){
                Venue venue = getVenueById(id);
                venues.add(venue);
            }
            return venues;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Venue","Place",place);
        }
    }


    public List<String> getVenueIdsByPlaceName(String place){
        try {
            String placeId = placeService.getPlaceIdByPlaceName(place);
            ArrayList<Value> res = faunaClient.query(
                    Paginate(Match(Index("venue_by_placeId"), Value(placeId)))
            ).get().at("data").get(ArrayList.class);
            List<String> venueIds = new ArrayList<>();
            for(int i=0;i<res.size();i++){
                String venueId = res.get(i).get(Value.RefV.class).getId();
                venueIds.add(venueId);
            }
            return venueIds;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Venue","Place",place);
        }
    }



    public String getVenueIdByVenueName(String venueName){
        String value = faunaClient.query(Get(Match(Index("venues_by_name"),
                Value(venueName)))).join().at("ref").get(Value.RefV.class).getId();
        return value;
    }


    @Override
    public boolean updateVenueById(String id, String name, String address, int capacity, String placeId) {
        try {
            getVenueById(id);
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
            return true;
        } catch (Exception e) {
                return false;
            }
        }catch (Exception e){
                throw new ResourceNotFoundException("Venue","Id",id);
            }
    }

    @Override
    public boolean deleteVenueById(String id) {
        try {
            getVenueById(id);
            try {
                Value val = faunaClient.query(Delete(Ref(Collection("Venue"), id))).get();
                if (val == null)
                    return false;
                return true;
            } catch (Exception e) {
                return false;
            }
        }catch (Exception e){
            throw new ResourceNotFoundException("Venue","Id",id);
        }
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

}
