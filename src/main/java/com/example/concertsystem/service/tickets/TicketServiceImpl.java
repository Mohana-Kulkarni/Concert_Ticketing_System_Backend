package com.example.concertsystem.service.tickets;

import com.example.concertsystem.entity.Ticket;
import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.service.event.EventService;
import com.example.concertsystem.service.event.EventServiceImpl;
import com.example.concertsystem.service.place.PlaceService;
import com.example.concertsystem.service.tier.TierService;
import com.example.concertsystem.service.user.UserService;
import com.example.concertsystem.service.venue.VenueService;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
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
        String tierId = tierService.getTierIdByTierName(tierName);
        String eventId = eventService.getEventIdByName(eventName);
        faunaClient.query(
                Create(
                        Collection("Ticket"),
                        Obj(
                                "data",
                                Obj(
                                        "count", Value(count),
                                        "userId", Value(userId),
                                        "tierId", Value(tierId),
                                        "eventId", Value(eventId)
                                )
                        )
                )
        );

        Tier tier = tierService.getTierById(tierId);
        tierService.updateTier(tierId, tier.name(), tier.capacity() - count, tier.price());

    }

    @Override
    public void updateTicket(String id, int count, String userName, String tierName, String eventName) throws ExecutionException, InterruptedException {
        String userId = userService.getIdByUserName(userName);
        String tierId = tierService.getTierIdByTierName(tierName);
        String eventId = eventService.getEventIdByName(eventName);
        faunaClient.query(
                Update(Ref(Collection("Ticket"), id),
                        Obj(
                                "data", Obj(
                                        "count", Value(count),
                                        "userId", Value(userId),
                                        "tierId", Value(tierId),
                                        "eventId", Value(eventId)
                                )
                        )
                )
        ).get();
    }

    @Override
    public Ticket getTicketById(String id) throws ExecutionException, InterruptedException {
        Value res = faunaClient.query(Get(Ref(Collection("Ticket"), Value(id)))).get();

        return new Ticket(
                res.at("ref").to(Value.RefV.class).get().getId(),
                res.at("data", "count").to(Integer.class).get(),
                res.at("data", "userId").to(String.class).get(),
                res.at("data", "tierId").to(String.class).get(),
                res.at("data", "eventId").to(String.class).get()
        );

    }

    @Override
    public Ticket getTicketByUserName(String userName) throws ExecutionException, InterruptedException {
        String userId = userService.getIdByUserName(userName);
        Value res = faunaClient.query(Get(Match(Index("ticket_by_userId"), Value(userId)))).get();
        return new Ticket(
                res.at("ref").to(Value.RefV.class).get().getId(),
                res.at("data", "count").to(Integer.class).get(),
                res.at("data", "userId").to(String.class).get(),
                res.at("data", "tierId").to(String.class).get(),
                res.at("data", "eventId").to(String.class).get()
        );
    }

    @Override
    public void deleteTicketById(String id) {
        faunaClient.query(Delete(Ref(Collection("Ticket"), id)));

    }
}
