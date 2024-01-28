package com.example.concertsystem.service.event;
import com.example.concertsystem.dto.ArtistResponse;
import com.example.concertsystem.dto.EventResponse;
import com.example.concertsystem.dto.UserResponse;
import com.example.concertsystem.entity.Artist;
import com.example.concertsystem.entity.Event;
import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.entity.User;
import com.example.concertsystem.Wrapper.ListWrapper;
import com.example.concertsystem.service.artist.ArtistService;
import com.example.concertsystem.service.firebase.FirebaseService;
import com.example.concertsystem.service.tier.TierService;
import com.example.concertsystem.service.user.UserService;
import com.example.concertsystem.service.venue.VenueService;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.faunadb.client.query.Language.*;
import static com.faunadb.client.query.Language.Obj;

@Service
public class EventServiceImpl implements EventService{
    private FaunaClient faunaClient;
    private ArtistService artistService;
    private TierService tierService;
    private Logger logger = Logger.getLogger(EventServiceImpl.class.getName());
    private VenueService venueService;
    private CacheManager cacheManager;
    private FirebaseService firebaseService;
    public EventServiceImpl(FaunaClient faunaClient, ArtistService artistService, TierService tierService, VenueService venueService, CacheManager cacheManager, FirebaseService firebaseService) {
        this.faunaClient = faunaClient;
        this.artistService = artistService;
        this.tierService = tierService;
        this.venueService = venueService;
        this.cacheManager = cacheManager;
        this.firebaseService = firebaseService;
    }

    @Override
    public void addEvent(Event event) throws ExecutionException, InterruptedException, IOException {
//        String venueRef = venueService.getVenueByName(event.venueId()).id();
//        List<String> artistIds = artistService.addArtistList(event.artistList(), event.profileImages());
//        String eventId = getTiersAddedForEvent(event.tierList());
//        List<String> tierIds = tierService.getTierByEventId(eventId);
//        List<String> imageUrls = new ArrayList<>();
//        for (MultipartFile file : event.images()) {
//            String url = firebaseService.upload(file);
//            imageUrls.add(url);
//        }
//
//
//        Map<String, Object> eventData = new HashMap<>();
//        eventData.put("name", event.name());
//        eventData.put("dateAndTime", event.dateAndTime());
//        eventData.put("description", event.description());
//        eventData.put("duration", event.eventDuration());
//        eventData.put("images", imageUrls);
//        eventData.put("venueId", venueRef);
//        eventData.put("artistId", artistIds);
//        eventData.put("tierId", tierIds);
//
//        faunaClient.query(
//                Create(
//                        Collection("Event"),
//                        Obj("data", Value(eventData))
//                )
//        );
    }

    @Override
    public void addEvent2(Event event, List<String> imageUrls, List<String> profileImgUrls) throws ExecutionException, InterruptedException, IOException {
        List<String> tierIds = tierService.addNewTiers(event.tierList());
        List<String> artistIds = artistService.addArtistList(event.artistList(), profileImgUrls);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put("name", event.name());
        eventData.put("dateAndTime", event.dateAndTime());
        eventData.put("description", event.description());
        eventData.put("duration", event.eventDuration());
        eventData.put("images", imageUrls);
        eventData.put("venueId", event.venueId());
        eventData.put("artistId", artistIds);
        eventData.put("tierId", tierIds);

        String id = faunaClient.query(
                Create(
                        Collection("Event"),
                        Obj("data", Value(eventData))
                )
        ).join().at("ref").get(Value.RefV.class).getId();


//        EventResponse eventResponse = getEventById(id);
//        addEventToCachedList(getPlaceByEventId(id),eventResponse);

    }



    public void addEventToCachedList(String key, EventResponse newEvent) {
        Cache cache = cacheManager.getCache("eventCacheStore1");

        if (cache != null) {
            ListWrapper listWrapper = cache.get(key, ListWrapper.class);

            if (listWrapper != null) {
                List<EventResponse> currentList = listWrapper.getList();
                currentList.add(newEvent);
                cache.put(key, new ListWrapper(currentList));
            }
        }
    }

    @Override
//    @Cacheable(cacheNames = "eventCacheStore2",key = "#id")
    public EventResponse getEventById(String id) throws ExecutionException, InterruptedException, IOException {
        Value val = faunaClient.query(Get(Ref(Collection("Event"), id))).get();

        List<String> artists = val.at("data", "artistId").collect(String.class).stream().toList();
        List<String> tiers = val.at("data", "tierId").collect(String.class).stream().toList();
        List<String> imageUrls= val.at("data", "images").collect(String.class).stream().toList();

        List<ArtistResponse> artistList = artistService.getArtistsByIds(artists);
        List<Tier> tierObjList = tierService.getTierListByIds(tiers);

        return new EventResponse(
                id,
                val.at("data", "name").to(String.class).get(),
                val.at("data", "dateAndTime").to(String.class).get(),
                val.at("data", "description").to(String.class).get(),
                val.at("data", "duration").to(String.class).get(),
                imageUrls,
                val.at("data", "venueId").to(String.class).get(),
                artistList,
                tierObjList
        );
    }

    @Override
    public List<EventResponse> getAllEvents() throws ExecutionException, InterruptedException, IOException {
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
//                List<Value.StringV> artists = val.at("data", "artistId").to(List.class).get();
//                List<String> artistList = artists.stream().map(
//                                stringV -> stringV.to(String.class).get())
//                        .collect(Collectors.toList());
//                List<Value.StringV> tiers = val.at("data", "tierId").to(List.class).get();
//                List<String> tiersList = tiers.stream().map(
//                                stringV -> stringV.to(String.class).get())
//                        .collect(Collectors.toList());

                List<String> artists = val.at("data", "artistId").collect(String.class).stream().toList();
                List<String> tiers = val.at("data", "tierId").collect(String.class).stream().toList();
                List<String> imageUrls= val.at("data", "images").collect(String.class).stream().toList();


                EventResponse event = new EventResponse(
                        val.at("ref").to(Value.RefV.class).get().getId(),
                        val.at("data", "name").to(String.class).get(),
                        val.at("data", "dateAndTime").to(String.class).get(),
                        val.at("data", "description").to(String.class).get(),
                        val.at("data", "description").to(String.class).get(),
                        imageUrls,
                        val.at("data", "venueId").to(String.class).get(),
                        artistService.getArtistsByIds(artists),
                        tierService.getTierListByIds(tiers)
                );
                eventList.add(event);
            }

        }
        return eventList;
    }

    @Cacheable(cacheNames = "eventCacheStore1",key = "#place")
    public ListWrapper getEventByPlaceName(String place) throws ExecutionException, InterruptedException, IOException {
        List<EventResponse> res = getEventByPlace(place);
        return new ListWrapper(res);
    }

    @Override
    public List<EventResponse> getEventByPlace(String place) throws ExecutionException, InterruptedException, IOException {
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
        logger.info("GetMethod");
        return events;

    }

    @Cacheable(cacheNames = "eventCacheStore1",key = "#artist")
    public ListWrapper getEventByArtistName(String artist) throws ExecutionException, InterruptedException, IOException {
        List<EventResponse> res = getEventByArtist(artist);
        return new ListWrapper(res);
    }

    @Override
    public List<EventResponse> getEventByArtist(String artist) throws ExecutionException, InterruptedException, IOException {
        String artistRef = artistService.getArtistIdByName(artist);
        Value res = faunaClient.query(
                Map(
                        Paginate(
                                Match(Index("event_by_userId"), Value(artistRef))
                        ),
                        Lambda("eventRef", Get(Var("eventRef")))
                )
        ).get();
        List<Value> events = res.at("data").to(List.class).get();
        List<EventResponse> eventList = new ArrayList<>();
        for(Value event : events){
            List<String> artists = event.at("data", "artistId").collect(String.class).stream().toList();
            List<String> tiers = event.at("data", "tierId").collect(String.class).stream().toList();
            List<String> imageUrls= event.at("data", "images").collect(String.class).stream().toList();

            List<ArtistResponse> artistObjList = artistService.getArtistsByIds(artists);
            List<Tier> tierObjList = tierService.getTierListByIds(tiers);


            EventResponse eventByArt = new EventResponse(
                    event.at("ref").to(Value.RefV.class).get().getId(),
                    event.at("data", "name").to(String.class).get(),
                    event.at("data", "dateAndTime").to(String.class).get(),
                    event.at("data", "description").to(String.class).get(),
                    event.at("data", "duration").to(String.class).get(),
                    imageUrls,
                    event.at("data", "venueId").to(String.class).get(),
                    artistObjList,
                    tierObjList
            );
            eventList.add(eventByArt);
        }

        return eventList;
    }

    @Cacheable(cacheNames = "eventCacheStore1",key = "#venue")
    public ListWrapper getEventByVenueName(String venue) throws ExecutionException, InterruptedException, IOException {
        List<EventResponse> res = getEventByVenue(venue);
        return new ListWrapper(res);
    }
    @Override
    public List<EventResponse> getEventByVenue(String venue) throws ExecutionException, InterruptedException, IOException {
        String venueRef = venueService.getVenueIdByVenueName(venue);
        List<String> eventIds = getEventIdsByVenueId(venueRef);
        List<EventResponse> events = new ArrayList<>();
        for(String id : eventIds){
            EventResponse event = getEventById(id);
            events.add(event);
        }
        return events;
    }

//    @Override
//    @CachePut(cacheNames = "eventCacheStore2",key = "#id")
//    public void updateEvent(String id, String name, String date, String description, String eventDuration, String venueName, List<Artist> artistList, List<Tier> tierList) throws ExecutionException, InterruptedException {
//        String venueRef = venueService.getVenueByName(venueName).id();
//        List<String> userRefs = userService.getUserIdsByUserName(userId);
//        List<String> tierRefs = tierService.getIdByTierName(tierId);
//        Map<String, Object> eventData = new HashMap<>();
//        eventData.put("name", name);
//        eventData.put("dateAndTime", date);
//        eventData.put("description", description);
//        eventData.put("duration", eventDuration);
//        eventData.put("venueId", venueRef);
//        eventData.put("userId", userRefs);
//        eventData.put("tierId", tierRefs);
//        faunaClient.query(
//                Update(
//                        Ref(Collection("Event"), id),
//                        Obj("data", Value(eventData))
//                )
//        );
//
//    }

    public void updateEventCache(String place, String id) throws ExecutionException, InterruptedException {
        Cache cache = cacheManager.getCache("eventCacheStore1");

        if (cache != null) {
            ListWrapper listWrapper = cache.get(place, ListWrapper.class);

            if (listWrapper != null) {
                List<EventResponse> currentList = listWrapper.getList();
                EventResponse objectToRemove = currentList.stream()
                        .filter(event -> id.equals(event.getId()))
                        .findFirst()
                        .orElse(null);

                if (objectToRemove != null) {
                    currentList.remove(objectToRemove);
                    cache.put(place, new ListWrapper(currentList));
                }
            }
        }
    }

    public void deleteEventPlaceCache(String place, String id) throws ExecutionException, InterruptedException {
        updateEventCache(place, id);
    }

    @Override
    @CacheEvict(cacheNames = "eventCacheStore2", key = "#id")
    public void deleteEventById(String id) throws ExecutionException, InterruptedException {
        String place = getPlaceByEventId(id);
        faunaClient.query(Delete(Ref(Collection("Event"), id)));
        deleteEventPlaceCache(place,id);

    }

    @Override
    public String getPlaceByEventId(String EventId){
        String venueId = faunaClient.query(Get(Ref(Collection("Event"), Value(EventId)))).join().at("data").at("venueId").get(String.class);
        String placeId = faunaClient.query(Get(Ref(Collection("Venue"), Value(venueId)))).join().at("data").at("placeId").get(String.class);
        return faunaClient.query(Get(Ref(Collection("Place"), Value(placeId)))).join().at("data").at("name").get(String.class);
    }

    @Override
    public String getEventIdByName(String eventName) throws ExecutionException, InterruptedException {
        return faunaClient.query(Get(Match(Index("event_by_eventName"), Value(eventName)))).get().at("ref").to(Value.RefV.class).get().getId();
    }

    private String getTiersAddedForEvent(List<Tier> tierList) throws ExecutionException, InterruptedException {
        Random random = new Random();
        int n = random.nextInt(9000) + 1000;
        String eventId = "event" + n;
        System.out.println(eventId);
        for (Tier tier : tierList) {
            tierService.addTier(tier.name(), tier.capacity(), tier.price());
        }
        return eventId;
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


}
