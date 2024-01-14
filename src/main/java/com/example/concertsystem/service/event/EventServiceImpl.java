package com.example.concertsystem.service.event;

import com.example.concertsystem.entity.Event;
import com.example.concertsystem.entity.User;
import com.example.concertsystem.service.place.PlaceService;
import com.example.concertsystem.service.tier.TierService;
import com.example.concertsystem.service.user.UserService;
import com.example.concertsystem.service.venue.VenueService;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.query.Expr;
import com.faunadb.client.query.Language;
import com.faunadb.client.types.Field;
import com.faunadb.client.types.Value;

import org.springframework.stereotype.Service;

import java.security.Provider;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.faunadb.client.query.Language.*;
import static com.faunadb.client.query.Language.Obj;

@Service
public class EventServiceImpl implements EventService{
    private FaunaClient faunaClient;
    private UserService userService;
    private TierService tierService;
    private PlaceService placeService;

    private VenueService venueService;
    public EventServiceImpl(FaunaClient faunaClient, UserService userService, TierService tierService, VenueService venueService, PlaceService placeService) {
        this.faunaClient = faunaClient;
        this.userService = userService;
        this.tierService = tierService;
        this.venueService = venueService;
        this.placeService = placeService;
    }
    @Override
    public void addEvent(String name, String date, String description, String venueName, List<String> userId, List<String> tierId) throws ExecutionException, InterruptedException {
//        venueService.getVenueByName(venueName);
        String venueRef = venueService.getVenueByName(venueName).id();
        List<String> userRefs = userService.getUserIdsByUserName(userId);
        List<String> tierRefs = tierService.getIdByTierName(tierId);
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("name", name);
        eventData.put("dateAndTime", date);
        eventData.put("description", description);
        eventData.put("venueId", venueRef);
        eventData.put("userId", Collections.singletonList(String.valueOf(userRefs)));
        eventData.put("tierId", Collections.singletonList(String.valueOf(tierRefs)));

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
    public void updateEvent(String id, String name, String date, String description, String venueName, List<String> userId, List<String> tierId) throws ExecutionException, InterruptedException {
        String venueRef = venueService.getVenueByName(venueName).id();
        List<String> userRefs = userService.getUserIdsByUserName(userId);
        List<String> tierRefs = tierService.getIdByTierName(tierId);
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("name", name);
        eventData.put("dateAndTime", date);
        eventData.put("description", description);
        eventData.put("venueId", venueRef);
        eventData.put("userId", userRefs);
        eventData.put("tierId", tierRefs);
        faunaClient.query(
                Update(
                        Ref(Collection("Event"), id),
                        Obj("data", Value(eventData))
                )
        );
    }

    @Override
    public List<Event> getEventByArtist(String artist) throws ExecutionException, InterruptedException {
        String userRef = userService.getIdByUserName(artist);
        Value res = faunaClient.query(
                Map(
                        Paginate(
                                Match(Index("event_by_userId"), Value(userRef))
                        ),
                        Lambda("eventRef", Get(Var("eventRef")))
                )
        ).get();
        List<Value> events = res.at("data").to(List.class).get();
        List<Event> eventList = new ArrayList<>();
        for(Value event : events){
            Event eventByArt = new Event(
                    event.at("ref").to(Value.RefV.class).get().getId(),
                    event.at("data", "name").to(String.class).get(),
                    event.at("data", "dateAndTime").to(String.class).get(),
                    event.at("data", "description").to(String.class).get(),
                    event.at("data", "venueId").to(String.class).get(),
                    Collections.singletonList(String.valueOf(event.at("data", "userId").to(List.class).get())),
                    Collections.singletonList(String.valueOf(event.at("data", "tierId").to(List.class).get()))
            );
            eventList.add(eventByArt);
        }
        return eventList;
    }

    @Override
    public List<Event> getEventByPlace(String place) throws ExecutionException, InterruptedException {
        List<String> venueIds = venueService.getVenueIdsByPlaceName(place);
        List<String> eventIds = new ArrayList<>();
        for(String id:venueIds){
            eventIds.addAll(getEventIdsByVenueId(id));
        }
        List<Event> events = new ArrayList<>();
        for(String id : eventIds){
            Event event = getEventById(id);
            events.add(event);
        }
        return events;
    }

    @Override
    public Event getEventById(String id) throws ExecutionException, InterruptedException {
        Value res = faunaClient.query(Get(Ref(Collection("Event"), id))).get();

        return new Event(
                id,
                res.at("data", "name").to(String.class).get(),
                res.at("data", "dateAndTime").to(String.class).get(),
                res.at("data", "description").to(String.class).get(),
                res.at("data", "venueId").to(String.class).get(),
                Collections.singletonList(String.valueOf(res.at("data", "userId").to(List.class).get())),
                Collections.singletonList(String.valueOf(res.at("data", "tierId").to(List.class).get()))
        );
    }

    @Override
    public void deleteEventById(String id) {
        faunaClient.query(Delete(Ref(Collection("Event"), id)));
    }

    @Override
    public List<Event> getEventByVenue(String venue) throws ExecutionException, InterruptedException {
        String venueRef = venueService.getVenueIdByVenueName(venue);
        List<String> eventIds = getEventIdsByVenueId(venueRef);
        List<Event> events = new ArrayList<>();
        for(String id : eventIds){
            Event event = getEventById(id);
            events.add(event);
        }
        return events;
    }

    @Override
    public String getEventIdByName(String eventName) throws ExecutionException, InterruptedException {
        return faunaClient.query(Get(Match(Index("event_by_eventName"), Value(eventName)))).get().at("ref").to(Value.RefV.class).get().getId();
    }

    private List<String> getEventIdsByVenueId(String id) throws ExecutionException, InterruptedException {
        ArrayList<Value> res = faunaClient.query(
                Paginate(Match(Index("event_by_venueId"), Value(id)))
        ).get().at("data").get(ArrayList.class);
        List<String> eventIds = new ArrayList<>();
        for(int i=0;i<res.size();i++){
            String eventId = res.get(i).get(Value.RefV.class).getId();
            eventIds.add(eventId);
        }
        return eventIds;
    }
}
