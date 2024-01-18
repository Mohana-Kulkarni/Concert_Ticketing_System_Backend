package com.example.concertsystem.service.event;

import com.example.concertsystem.dto.EventResponse;
import com.example.concertsystem.entity.Event;
import com.example.concertsystem.entity.Tier;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.faunadb.client.query.Language.*;
import static com.faunadb.client.query.Language.Obj;

@Service
public class EventServiceImpl implements EventService{
    private FaunaClient faunaClient;
    private UserService userService;
    private TierService tierService;

    private VenueService venueService;
    public EventServiceImpl(FaunaClient faunaClient, UserService userService, TierService tierService, VenueService venueService) {
        this.faunaClient = faunaClient;
        this.userService = userService;
        this.tierService = tierService;
        this.venueService = venueService;
    }
    @Override
    public void addEvent(String name, String date, String description, String eventDuration, String venueName, List<String> userId, List<String> tierId) throws ExecutionException, InterruptedException {
//        venueService.getVenueByName(venueName);
        System.out.println(eventDuration);
        String venueRef = venueService.getVenueByName(venueName).id();
        List<String> userRefs = userService.getUserIdsByUserName(userId);
        List<String> tierRefs = tierService.getIdByTierName(tierId);
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("name", name);
        eventData.put("dateAndTime", date);
        eventData.put("description", description);
        eventData.put("duration", eventDuration);
        eventData.put("venueId", venueRef);
        eventData.put("userId", userRefs);
        eventData.put("tierId", tierRefs);

        faunaClient.query(
                Create(
                        Collection("Event"),
                        Obj("data", Value(eventData))
                )
        );
        System.out.println(Value(eventData));
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
    public void updateEvent(String id, String name, String date, String description, String eventDuration, String venueName, List<String> userId, List<String> tierId) throws ExecutionException, InterruptedException {
        String venueRef = venueService.getVenueByName(venueName).id();
        List<String> userRefs = userService.getUserIdsByUserName(userId);
        List<String> tierRefs = tierService.getIdByTierName(tierId);
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("name", name);
        eventData.put("dateAndTime", date);
        eventData.put("description", description);
        eventData.put("duration", eventDuration);
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
    public List<EventResponse> getEventByArtist(String artist) throws ExecutionException, InterruptedException {
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
        List<EventResponse> eventList = new ArrayList<>();
        for(Value event : events){
            List<String> artists = event.at("data", "userId").collect(String.class).stream().toList();
            List<String> tiers = event.at("data", "tierId").collect(String.class).stream().toList();

            List<User> artistObjList = userService.getUserListById(artists);
            List<Tier> tierObjList = tierService.getTierListByIds(tiers);

            EventResponse eventByArt = new EventResponse(
                    event.at("ref").to(Value.RefV.class).get().getId(),
                    event.at("data", "name").to(String.class).get(),
                    event.at("data", "dateAndTime").to(String.class).get(),
                    event.at("data", "description").to(String.class).get(),
                    event.at("data", "duration").to(String.class).get(),
                    event.at("data", "venueId").to(String.class).get(),
                    artistObjList,
                    tierObjList
            );
            eventList.add(eventByArt);
        }
        return eventList;
    }

    @Override
    public List<EventResponse> getEventByPlace(String place) throws ExecutionException, InterruptedException {
        List<String> venueIds = venueService.getVenueIdsByPlaceName(place);
        List<String> eventIds = new ArrayList<>();
        for(String id:venueIds){
            eventIds.addAll(getEventIdsByVenueId(id));
        }
        List<EventResponse> events = new ArrayList<>();
        for(String id : eventIds){
            EventResponse event = getEventById(id);
            events.add(event);
        }
        return events;
    }

    @Override
    public EventResponse getEventById(String id) throws ExecutionException, InterruptedException {
        CompletableFuture<Value> res = faunaClient.query(Get(Ref(Collection("Event"), id)));
        Value val = res.join();

        List<String> artists = val.at("data", "userId").collect(String.class).stream().toList();
        List<String> tiers = val.at("data", "tierId").collect(String.class).stream().toList();

        List<User> artistObjList = userService.getUserListById(artists);
        List<Tier> tierObjList = tierService.getTierListByIds(tiers);


        return new EventResponse(
                id,
                val.at("data", "name").to(String.class).get(),
                val.at("data", "dateAndTime").to(String.class).get(),
                val.at("data", "description").to(String.class).get(),
                val.at("data", "duration").to(String.class).get(),
                val.at("data", "venueId").to(String.class).get(),
                artistObjList,
                tierObjList
        );
    }

    @Override
    public void deleteEventById(String id) {
        faunaClient.query(Delete(Ref(Collection("Event"), id)));
    }

    @Override
    public List<EventResponse> getEventByVenue(String venue) throws ExecutionException, InterruptedException {
        String venueRef = venueService.getVenueIdByVenueName(venue);
        List<String> eventIds = getEventIdsByVenueId(venueRef);
        List<EventResponse> events = new ArrayList<>();
        for(String id : eventIds){
            EventResponse event = getEventById(id);
            events.add(event);
        }
        return events;
    }

    @Override
    public String getEventIdByName(String eventName) throws ExecutionException, InterruptedException {
        return faunaClient.query(Get(Match(Index("event_by_eventName"), Value(eventName)))).get().at("ref").to(Value.RefV.class).get().getId();
    }

    @Override
    public List<EventResponse> getAllEvents() throws ExecutionException, InterruptedException {
        List<Value> res = (List<Value>) faunaClient.query(
                Map(
                        Paginate(Documents(Collection("Event"))),
                        Lambda("eventRef", Get(Var("eventRef")))
                )
        ).get();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
        LocalDateTime now = LocalDateTime.now();
        String dateString = now.format(formatter);
        System.out.println(dateString);

        LocalDate date1 = LocalDate.parse(dateString, formatter);

        List<EventResponse> eventList = new ArrayList<>();
        for(Value val : res) {
            String eventDate = val.at("data", "dateAndTime").to(String.class).get();
            LocalDateTime dateTime = LocalDateTime.parse(eventDate, dtFormatter);
            String date = dateTime.format(formatter);

            LocalDate date2 = LocalDate.parse(dateString, formatter);

            if(date1.isBefore(date2) || date1.equals(date2)) {
                List<Value.StringV> artists = val.at("data", "userId").to(List.class).get();
                List<String> artistList = artists.stream().map(
                                stringV -> stringV.to(String.class).get())
                        .collect(Collectors.toList());
                List<Value.StringV> tiers = val.at("data", "tierId").to(List.class).get();
                List<String> tiersList = tiers.stream().map(
                                stringV -> stringV.to(String.class).get())
                        .collect(Collectors.toList());


                EventResponse event = new EventResponse(
                        val.at("ref").to(Value.RefV.class).get().getId(),
                        val.at("data", "name").to(String.class).get(),
                        val.at("data", "dateAndTime").to(String.class).get(),
                        val.at("data", "description").to(String.class).get(),
                        val.at("data", "description").to(String.class).get(),
                        val.at("data", "venueId").to(String.class).get(),
                        userService.getUserListById(artistList),
                        tierService.getTierListByIds(tiersList)
                );
                eventList.add(event);
            }

        }
        return eventList;
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
