package com.example.concertsystem.service.organiser;

import com.example.concertsystem.dto.EventImageResponse;
import com.example.concertsystem.dto.EventResponse;
import com.example.concertsystem.dto.OrganiserResponse;
import com.example.concertsystem.dto.UserResponse;
import com.example.concertsystem.entity.Organiser;
import com.example.concertsystem.exception.ResourceNotFoundException;
import com.example.concertsystem.exception_handling.classes.OrganiserNotFoundException;
import com.example.concertsystem.service.event.EventService;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.faunadb.client.query.Language.*;
import static com.faunadb.client.query.Language.Value;

@Service
public class OrganiserServiceImpl implements OrganiserService{
    private FaunaClient faunaClient;

    private EventService eventService;
    public OrganiserServiceImpl(FaunaClient faunaClient, EventService eventService) {
        this.faunaClient= faunaClient;
        this.eventService = eventService;
    }
    @Override
    public boolean addOrganiser(String name, String email, String govId, String walletId, String transactionId, String profileImg) {
        try {
            Map<String, Object> organiserData = new HashMap<>();
            organiserData.put("name", name);
            organiserData.put("email", email);
            organiserData.put("govId", govId);
            organiserData.put("walletId", walletId);
            organiserData.put("transactionId", transactionId);
            organiserData.put("profileImg", profileImg);
            organiserData.put("organisedEvents", new ArrayList<>());
            faunaClient.query(
                    Create(
                            Collection("Organiser"),
                            Obj(
                                    "data", Value(organiserData)
                            )
                    )
            );
            return true;
        }catch (Exception e){
            return false;
        }
    }
    @Override
    public OrganiserResponse isOrganiserRegistered(String walletId){
        try {
            Value val = faunaClient.query(
                    Get(
                            Match(Index("organiser_by_walletId"), Value(walletId))
                    )
            ).get();
            List<String> events = val.at("data", "organisedEvents").collect(String.class).stream().toList();
            return new OrganiserResponse(
                    val.at("ref").to(Value.RefV.class).get().getId(),
                    val.at("data", "name").to(String.class).get(),
                    val.at("data", "email").to(String.class).get(),
                    val.at("data","govId").to(String.class).get(),
                    val.at("data", "walletId").to(String.class).get(),
                    val.at("data", "transactionId").to(String.class).get(),
                    val.at("data", "profileImg").to(String.class).get(),
                    events
            );
        } catch (Exception e) {
            throw new ResourceNotFoundException("Organiser","walletId", walletId);
        }

    }

    @Override
    public boolean createEvent(EventImageResponse eventImageResponse, String organiserId) {
        String id = null;
        try {
            Map<String, String> map = eventService.addEvent2(eventImageResponse.getEvent(), eventImageResponse.getImgUrls());
            if(map.get("result").equals("true")) {
                if(map.get("id") != null) {
                    id = map.get("id");
                }
            }
            updateOrganiser(organiserId, id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<EventResponse> getEventsAddedByOrganiser(String id) {
        OrganiserResponse organiser = getOrganiserById(id);
        List<String> eventIds = new ArrayList<>();
        eventIds.addAll(organiser.organisedEvents());
        return eventService.getEventsByIdList(eventIds);
    }

    @Override
    public OrganiserResponse getOrganiserById(String id){
        try {
            Value res = faunaClient.query(Get(Ref(Collection("Organiser"), id))).get();

            return new OrganiserResponse(
                    res.at("ref").to(Value.RefV.class).get().getId(),
                    res.at("data", "name").to(String.class).get(),
                    res.at("data", "email").to(String.class).get(),
                    res.at("data", "govId").to(String.class).get(),
                    res.at("data", "walletId").to(String.class).get(),
                    res.at("data", "transactionId").to(String.class).get(),
                    res.at("data", "profileImg").to(String.class).get(),
                    res.at("data", "organisedEvents").collect(String.class).stream().toList()
            );
        } catch (Exception e) {
            throw new ResourceNotFoundException("Organiser","Id",id);
        }
    }

    @Override
    public boolean updateOrganiser(String id, String eventId){
        try {
            OrganiserResponse organiser = getOrganiserById(id);
            List<String> organisedEvents = new ArrayList<>();
            organisedEvents.addAll(organiser.organisedEvents());
            organisedEvents.add(eventId);

            try {
                Map<String, Object> organiserData = new HashMap<>();
                organiserData.put("name", organiser.name());
                organiserData.put("email", organiser.email());
                organiserData.put("govId", organiser.govId());
                organiserData.put("walletId", organiser.walletId());
                organiserData.put("transactionId", organiser.transactionId());
                organiserData.put("profileImg", organiser.profileImg());
                organiserData.put("organisedEvents", organisedEvents);
                faunaClient.query(
                        Update(Ref(Collection("Organiser"), id),
                                Obj(
                                        "data", Value(organiserData)
                                )
                        )
                ).get();
                return true;
            } catch (Exception e) {
                return false;
            }
        }catch (Exception e){
            throw new ResourceNotFoundException("Organiser","Id",id);
        }
    }

    @Override
    public boolean updateOrgqniserProfile(String id, Organiser organiser) {
        try {
            faunaClient.query(
                    Update(Ref(Collection("Organiser"), id),
                            Obj(
                                    "data", Value(organiser)
                            )
                    )
            ).get();
            return true;
        }catch (Exception e){
            throw new ResourceNotFoundException("Organiser","Id",id);
        }
    }

    @Override
    public boolean deleteOrganiser(String id) {
        try {
            getOrganiserById(id);
            try {
                Value val = faunaClient.query(Delete(Ref(Collection("Organiser"), id))).get();
                if(val==null){
                    return false;
                }
                return true;

            } catch (Exception e) {
                return false;
            }
        }catch (Exception e){
            throw new ResourceNotFoundException("Organiser","Id",id);
        }
    }
}
