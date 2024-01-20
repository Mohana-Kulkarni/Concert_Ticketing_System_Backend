package com.example.concertsystem.service.tickets;

import com.example.concertsystem.dto.EventResponse;
import com.example.concertsystem.dto.TicketResponse;
import com.example.concertsystem.entity.Event;
import com.example.concertsystem.entity.Ticket;
import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.entity.User;
import com.example.concertsystem.service.event.EventService;
import com.example.concertsystem.service.tier.TierService;
import com.example.concertsystem.service.user.UserService;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.faunadb.client.query.Language.*;
import static com.faunadb.client.query.Language.Value;


@Service
public class TicketServiceImpl implements TicketService{

    private FaunaClient faunaClient;
    private UserService userService;
    private TierService tierService;
    private EventService eventService;
    public TicketServiceImpl(FaunaClient faunaClient, UserService userService, TierService tierService, EventService eventService) {
        this.faunaClient = faunaClient;
        this.userService = userService;
        this.tierService = tierService;
        this.eventService = eventService;
    }
    @Override
    public void generateTicket(int count, String userName, String tierName, String eventName) throws ExecutionException, InterruptedException {
        String userId = userService.getIdByUserName(userName);
        String eventId = eventService.getEventIdByName(eventName);
        List<Tier> tierList = eventService.getEventById(eventId).tierId();
        Tier newTier = null;
        for(Tier tier : tierList) {
            if((tier.name()).equals(tierName)) {
                newTier = tier;
                faunaClient.query(
                        Create(
                                Collection("Ticket"),
                                Obj(
                                        "data",
                                        Obj(
                                                "count", Value(count),
                                                "cost", Value((long) count * tier.price()),
                                                "userId", Value(userId),
                                                "tierId", Value(tier.id()),
                                                "eventId", Value(eventId)
                                        )
                                )
                        )
                );
                break;
            }
        }
        int updatedCapacity = newTier.capacity() - count;
        tierService.updateTier(newTier.id(), tierName, updatedCapacity, newTier.price());

    }

    @Override
    public void updateTicket(String id, int count, String userName, String tierName, String eventName) throws ExecutionException, InterruptedException {
        String userId = userService.getIdByUserName(userName);
        String eventId = eventService.getEventIdByName(eventName);
        List<Tier> tierList = eventService.getEventById(eventId).tierId();
        for (Tier tier : tierList) {
            if((tier.name()).equals(tierName)) {
                faunaClient.query(
                        Update(Ref(Collection("Ticket"), id),
                                Obj(
                                        "data", Obj(
                                                "count", Value(count),
                                                "cost", Value((long) count * tier.price()),
                                                "userId", Value(userId),
                                                "tierId", Value(tier.id()),
                                                "eventId", Value(eventId)
                                        )
                                )
                        )
                ).get();
                break;
            }
        }

    }

    @Override
    public TicketResponse getTicketById(String id) throws ExecutionException, InterruptedException {
        Value res = faunaClient.query(Get(Ref(Collection("Ticket"), Value(id)))).get();
        User user = userService.getUserById(res.at("data", "userId").to(String.class).get());
        Tier tier = tierService.getTierById(res.at("data", "tierId").to(String.class).get());
        EventResponse event = eventService.getEventById(res.at("data", "eventId").to(String.class).get());
        return new TicketResponse(
                res.at("ref").to(Value.RefV.class).get().getId(),
                res.at("data", "count").to(Integer.class).get(),
                res.at("data", "cost").to(Integer.class).get(),
                user,
                tier,
                event
        );

    }

    @Override
    public TicketResponse getTicketByUserName(String userName) throws ExecutionException, InterruptedException {
        String userId = userService.getIdByUserName(userName);
        Value res = faunaClient.query(Get(Match(Index("ticket_by_userId"), Value(userId)))).get();
        User user = userService.getUserById(res.at("data", "userId").to(String.class).get());
        Tier tier = tierService.getTierById(res.at("data", "tierId").to(String.class).get());
        EventResponse event = eventService.getEventById(res.at("data", "eventId").to(String.class).get());
        return new TicketResponse(
                res.at("ref").to(Value.RefV.class).get().getId(),
                res.at("data", "count").to(Integer.class).get(),
                res.at("data", "cost").to(Integer.class).get(),
                user,
                tier,
                event
        );
    }

    @Override
    public void deleteTicketById(String id) {
        faunaClient.query(Delete(Ref(Collection("Ticket"), id)));

    }
}
