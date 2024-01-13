package com.example.concertsystem.service.event;

import com.example.concertsystem.entity.Event;
import com.example.concertsystem.service.tier.TierService;
import com.example.concertsystem.service.user.UserService;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.query.Expr;
import com.faunadb.client.query.Language;
import com.faunadb.client.types.Value;
import org.springframework.stereotype.Service;

import java.security.Provider;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.faunadb.client.query.Language.*;
import static com.faunadb.client.query.Language.Obj;

@Service
public class EventServiceImpl implements EventService{
    private FaunaClient faunaClient;
    private UserService userService;
    private TierService tierService;
    public EventServiceImpl(FaunaClient faunaClient, UserService userService, TierService tierService) {
        this.faunaClient = faunaClient;
        this.userService = userService;
        this.tierService = tierService;
    }
    @Override
    public void addEvent(String name, String date, String description, String venueId, List<String> userId, List<String> tierId) {
        String venueRef = getVenueRef(venueId);
        List<String> userRefs = userService.getUserIdByUserName(userId);
        List<String> tierRefs = tierService.getIdByTierName(tierId);
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("name", name);
        eventData.put("dateAndTime", date);
        eventData.put("description", description);
        eventData.put("venueId", venueRef);
        eventData.put("userId", userRefs);
        eventData.put("tierId", tierRefs);

        faunaClient.query(
                Create(
                        Collection("Event"),
                        Obj("data", Value(eventData))
                )
        );
    }



    private String getVenueRef(String venueId) {
        Value result = faunaClient.query(Get(Ref(Collection("Venue"), venueId))).join();
        try {
            Value res = result.at("ref").to(Value.RefV.class).get();
            System.out.println("The ref is : "  + res);
            return String.valueOf(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Event updateEvent(String id, Event event) {
        return null;
    }

    @Override
    public Event getEventByArtist(String artist) {
        return null;
    }

    @Override
    public Event getEventByPlace(String place) {
        return null;
    }

    @Override
    public Event getEventById(String id) {
        return null;
    }

    @Override
    public void deleteEventById(String id) {

    }

    @Override
    public Event getEventByVenue(String venue) {
        return null;
    }
}
