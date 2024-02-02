package com.example.concertsystem.service.event;
import com.example.concertsystem.dto.ArtistResponse;
import com.example.concertsystem.dto.EventResponse;
import com.example.concertsystem.dto.UserResponse;
import com.example.concertsystem.entity.Artist;
import com.example.concertsystem.entity.Event;
import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.entity.User;
import com.example.concertsystem.Wrapper.ListWrapper;
import com.example.concertsystem.exception.classes.EventNotFoundException;
import com.example.concertsystem.service.artist.ArtistService;
import com.example.concertsystem.service.firebase.FirebaseService;
import com.example.concertsystem.service.tier.TierService;
import com.example.concertsystem.service.user.UserService;
import com.example.concertsystem.service.venue.VenueService;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.query.Expr;
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
    public void addEvent2(Event event, List<String> imageUrls, List<String> profileImgUrls) throws ExecutionException, InterruptedException {
        List<String> tierIds = tierService.addNewTiers(event.tierList());
        List<String> artistIds = artistService.addArtistList(event.artistList(), profileImgUrls);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put("name", event.name());
        eventData.put("dateAndTime", event.dateAndTime());
        eventData.put("description", event.description());
        eventData.put("duration", event.eventDuration());
        eventData.put("images", imageUrls);
        eventData.put("categories", event.categoryList());
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
    @Cacheable(cacheNames = "eventCacheStore2",key = "#id")
    public EventResponse getEventById(String id) throws ExecutionException, InterruptedException {
        try{
            Value val = faunaClient.query(Get(Ref(Collection("Event"), id))).get();

            List<String> artists = val.at("data", "artistId").collect(String.class).stream().toList();
            List<String> tiers = val.at("data", "tierId").collect(String.class).stream().toList();
            List<String> imageUrls= val.at("data", "images").collect(String.class).stream().toList();
            List<String> categoryList= val.at("data", "categories").collect(String.class).stream().toList();

            List<ArtistResponse> artistList = artistService.getArtistsByIds(artists);
            List<Tier> tierObjList = tierService.getTierListByIds(tiers);
            return new EventResponse(
                    id,
                    val.at("data", "name").to(String.class).get(),
                    val.at("data", "dateAndTime").to(String.class).get(),
                    val.at("data", "description").to(String.class).get(),
                    val.at("data", "duration").to(String.class).get(),
                    imageUrls,
                    categoryList,
                    val.at("data", "venueId").to(String.class).get(),
                    artistList,
                    tierObjList
            );
        } catch (Exception e) {
            throw new EventNotFoundException("Event id not found - " + id);
        }
    }

    @Override
    public List<EventResponse> getAllEvents() throws ExecutionException, InterruptedException {
        Value value = faunaClient.query(
                        Paginate(Documents(Collection("Event")))
        ).get();
        List<Value> res = value.at("data").collect(Value.class).stream().toList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
        LocalDateTime now = LocalDateTime.now();
        String dateString = now.format(formatter);
        System.out.println(dateString);

        LocalDate date1 = LocalDate.parse(dateString, formatter);
        List<EventResponse> eventList = new ArrayList<>();
        System.out.println(res);
        for(Value val : res) {
            String eventId = val.get(Value.RefV.class).getId();
            EventResponse event = getEventById(eventId);
            String eventDate = event.dateAndTime();
            LocalDateTime dateTime = LocalDateTime.parse(eventDate, dtFormatter);
            String date = dateTime.format(formatter);

            LocalDate date2 = LocalDate.parse(dateString, formatter);

            if(date1.isBefore(date2) || date1.equals(date2)) {
                eventList.add(event);
            }

        }
        return eventList;
    }

    @Cacheable(cacheNames = "eventCacheStore1",key = "#place")
    public ListWrapper getEventByPlaceName(String place) {
        List<EventResponse> res = getEventByPlace(place);
        return new ListWrapper(res);

    }

    @Override
    public List<EventResponse> getEventByPlace(String place) throws EventNotFoundException {
        try {
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
        } catch (Exception e) {
            throw new EventNotFoundException("Event with place not found - " + place);
        }

    }

    @Cacheable(cacheNames = "eventCacheStore1",key = "#artist")
    public ListWrapper getEventByArtistName(String artist) throws ExecutionException, InterruptedException, IOException {
        List<EventResponse> res = getEventByArtist(artist);
        return new ListWrapper(res);
    }

    @Override
    public List<EventResponse> getEventByArtist(String artist) throws ExecutionException, InterruptedException, IOException {
        try {
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
                        event.at("data", "categories").collect(String.class).stream().toList(),
                        event.at("data", "venueId").to(String.class).get(),
                        artistObjList,
                        tierObjList
                );
                eventList.add(eventByArt);
            }

            return eventList;
        } catch (Exception e) {
            throw new EventNotFoundException("Event with artist not found - " + artist);
        }
    }

    @Cacheable(cacheNames = "eventCacheStore1",key = "#venue")
    public ListWrapper getEventByVenueName(String venue) throws ExecutionException, InterruptedException, IOException {
        List<EventResponse> res = getEventByVenue(venue);
        return new ListWrapper(res);
    }
    @Override
    public List<EventResponse> getEventByVenue(String venue) throws ExecutionException, InterruptedException, IOException {
        try {
            String venueRef = venueService.getVenueIdByVenueName(venue);
            List<String> eventIds = getEventIdsByVenueId(venueRef);
            List<EventResponse> events = new ArrayList<>();
            for(String id : eventIds){
                EventResponse event = getEventById(id);
                events.add(event);
            }
            return events;
        } catch (Exception e) {
            throw new EventNotFoundException("Event with venue not found - " + venue);
        }
    }

    @Override
    public List<EventResponse> getSimilarEvents(String eventId) throws  ExecutionException, InterruptedException {
        try {
            EventResponse eventResponse = getEventById(eventId);
            List<String> categoryList = eventResponse.categoryList();

            List<EventResponse> events = getAllEvents();
            List<EventResponse> relatedPosts = new ArrayList<>();
            Map<String, Double> map = new HashMap<>();
            System.out.println(events.size());

            for(EventResponse event : events) {
                if(!event.id().equals(eventId)) {
                    double similarityScore = calculateCategorySimlarity(categoryList, event.categoryList());
                    map.put(event.id(), similarityScore);
                }
            }
            Map<String, Double> finalMap = sortByScore(map);
            for (String id : finalMap.keySet()) {
                EventResponse response = getEventById(id);
                relatedPosts.add(response);
            }
            return relatedPosts;
        } catch (Exception e) {
            throw new EventNotFoundException("Event id not found - "+ eventId);
        }
    }



    @Override
    @CachePut(cacheNames = "eventCacheStore2",key = "#id")
    public void updateEvent(String id, Event event, List<String> imageUrls, List<String> profileImgUrls) throws ExecutionException, InterruptedException {
        try {
            List<String> tierIds = tierService.addNewTiers(event.tierList());
            List<String> artistIds = artistService.addArtistList(event.artistList(), profileImgUrls);

            Map<String, Object> eventData = new HashMap<>();
            eventData.put("name", event.name());
            eventData.put("dateAndTime", event.dateAndTime());
            eventData.put("description", event.description());
            eventData.put("duration", event.eventDuration());
            eventData.put("images", imageUrls);
            eventData.put("categories", event.categoryList());
            eventData.put("venueId", event.venueId());
            eventData.put("artistId", artistIds);
            eventData.put("tierId", tierIds);
            faunaClient.query(
                    Update(
                            Ref(Collection("Event"), id),
                            Obj("data", Value(eventData))
                    )
            ).join();

        } catch (Exception e) {
            throw new EventNotFoundException("Event id not found - "+ id);
        }
    }

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
        try {
            String place = getPlaceByEventId(id);
            faunaClient.query(Delete(Ref(Collection("Event"), id)));
            deleteEventPlaceCache(place,id);

        } catch (Exception e) {
            throw new EventNotFoundException("Event id not found - "+ id);
        }
    }

    @Override
    public String getPlaceByEventId(String eventId){
        try {
            String venueId = faunaClient.query(Get(Ref(Collection("Event"), Value(eventId)))).join().at("data").at("venueId").get(String.class);
            String placeId = faunaClient.query(Get(Ref(Collection("Venue"), Value(venueId)))).join().at("data").at("placeId").get(String.class);
            return faunaClient.query(Get(Ref(Collection("Place"), Value(placeId)))).join().at("data").at("name").get(String.class);
        } catch (Exception e) {
            throw new EventNotFoundException("Event id not found - "+ eventId);
        }
    }

    @Override
    public String getEventIdByName(String eventName) throws ExecutionException, InterruptedException {
        try {
            return faunaClient.query(Get(Match(Index("event_by_eventName"), Value(eventName)))).get().at("ref").to(Value.RefV.class).get().getId();
        } catch (Exception e) {
            throw new EventNotFoundException("Event with name not found - "+ eventName);
        }
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

    private double calculateCategorySimlarity(List<String> categories1, List<String> categories2) {
        Set<String> set1 = new HashSet<>(categories1);
        Set<String> set2 = new HashSet<>(categories2);

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        return (double) intersection.size()/ union.size();
    }

    private static Map<String, Double> sortByScore(Map<String, Double> unsortedMap) {
        List<Map.Entry<String, Double>> list = new LinkedList<>(unsortedMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue()); // Reversed comparison
            }
        });

        Map<String, Double> sortedMap = new LinkedHashMap<>();

        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }



}
