package com.example.concertsystem.service.tickets;

import com.example.concertsystem.dto.EventResponse;
import com.example.concertsystem.dto.TicketResponse;
import com.example.concertsystem.dto.UserResponse;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public void generateTicket(int count, String userId, String tierId, String eventId, String transactionId, String nftToken) throws ExecutionException, InterruptedException, IOException {
        Tier tier = tierService.getTierById(tierId);
        Map<String, Object> ticketData = new HashMap<>();
        ticketData.put("count", count);
        ticketData.put("cost", (tier.price() * count));
        ticketData.put("userId", userId);
        ticketData.put("tierId", tierId);
        ticketData.put("eventId", eventId);
        ticketData.put("transactionId", transactionId);
        ticketData.put("nftToken", nftToken);

        faunaClient.query(
                Create(
                        Collection("Ticket"),
                        Obj(
                                "data",
                                Value(ticketData)
                        )
                )
        );

        int updatedCapacity = tier.capacity() - count;
        tierService.updateTier(tierId, tier.name(), updatedCapacity, tier.price());

    }

    @Override
    public void updateTicket(String id, int count, String userId, String tierId, String eventId, String transactionId, String nftToken) throws ExecutionException, InterruptedException, IOException {
        Tier tier = tierService.getTierById(tierId);
        Map<String, Object> ticketData = new HashMap<>();
        ticketData.put("count", count);
        ticketData.put("cost", (tier.price() * count));
        ticketData.put("userId", userId);
        ticketData.put("tierId", tierId);
        ticketData.put("eventId", eventId);
        ticketData.put("transactionId", transactionId);
        ticketData.put("nftToken", nftToken);

        faunaClient.query(
                Create(
                        Collection("Ticket"),
                        Obj(
                                "data",
                                Value(ticketData)
                        )
                )
        );

        int updatedCapacity = tier.capacity() - count;
        tierService.updateTier(tierId, tier.name(), updatedCapacity, tier.price());

    }

    @Override
    public TicketResponse getTicketById(String id) throws ExecutionException, InterruptedException, IOException {
        Value res = faunaClient.query(Get(Ref(Collection("Ticket"), Value(id)))).get();
        UserResponse user = userService.getUserById(res.at("data", "userId").to(String.class).get());
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
    public TicketResponse getTicketByUserId(String userId) throws ExecutionException, InterruptedException, IOException {
        Value res = faunaClient.query(Get(Match(Index("ticket_by_userId"), Value(userId)))).get();
        UserResponse user = userService.getUserById(res.at("data", "userId").to(String.class).get());
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
