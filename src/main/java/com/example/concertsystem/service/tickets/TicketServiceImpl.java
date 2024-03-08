package com.example.concertsystem.service.tickets;

import com.example.concertsystem.dto.EventResponse;
import com.example.concertsystem.dto.TicketResponse;
import com.example.concertsystem.dto.UserResponse;
import com.example.concertsystem.entity.*;
import com.example.concertsystem.exception.ResourceNotFoundException;
import com.example.concertsystem.service.event.EventService;
import com.example.concertsystem.service.tier.TierService;
import com.example.concertsystem.service.user.UserService;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.query.Pagination;
import com.faunadb.client.types.Value;
import com.google.api.MetricDescriptor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
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
    public boolean generateTicket(int count, String userId, String tierId, String eventId, String transactionId, List<String> nftToken){
        try {
            Tier tier = tierService.getTierById(tierId);
            try {
                int cost = tier.price() * count;
                Map<String, Object> ticketData = new HashMap<>();
                ticketData.put("count", count);
                ticketData.put("cost", cost);
                ticketData.put("userId", userId);
                ticketData.put("tierId", tierId);
                ticketData.put("eventId", eventId);
                ticketData.put("transactionId", transactionId);
                Map<String, Boolean> nftData = new HashMap<>();
                for(String nft : nftToken) {
                    nftData.put(nft, false);
                }
                ticketData.put("nftToken", nftData);


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
                return true;
            } catch (Exception e) {
                return false;
            }
        }catch (Exception e){
            throw new ResourceNotFoundException("Tier", "TierId", tierId);
        }


    }

    @Override
    public Map<String, String> updateTicket(String id, String nftId){
        try {
            TicketResponse ticket = getTicketById(id);
            Map<String, String> res = new HashMap<>();

            if(!ticket.nfts().containsKey(nftId)) {
                res.put("result", "Fraud Ticket");
                return res;
            } else {
                Map<String, Boolean> map = ticket.nfts();
                if(map.get(nftId)) {
                    res.put("result", "Already Scanned Ticket");
                    return res;
                } else {
                    Map<String, Object> ticketData = new HashMap<>();
                    ticketData.put("count", ticket.count());
                    ticketData.put("cost", (ticket.cost()));
                    ticketData.put("userId", ticket.user().id());
                    ticketData.put("tierId", ticket.tier().id());
                    ticketData.put("eventId", ticket.eventId().id());
                    ticketData.put("transactionId", ticket.transactionId());
                    if(map.containsKey(nftId)) {
                        map.replace(nftId, true);
                    }
                    ticketData.put("nftToken", map);

                    faunaClient.query(
                            Update(
                                    Ref(Collection("Ticket"), Value(id)),
                                    Obj(
                                            "data",
                                            Value(ticketData)
                                    ))
                    );
                    res.put("result", "NFT Status updated!");
                }
            }
            return res;
        }catch (Exception e){
            throw new ResourceNotFoundException("Ticket", "Id", id);
        }

    }

    @Override
    public TicketResponse getTicketById(String id){
        try {
            Value res = faunaClient.query(Get(Ref(Collection("Ticket"), Value(id)))).get();
            UserResponse user = userService.getUserById(res.at("data", "userId").to(String.class).get());
            Tier tier = tierService.getTierById(res.at("data", "tierId").to(String.class).get());
            EventResponse event = eventService.getEventById(res.at("data", "eventId").to(String.class).get());
            Map<String, Value> nftTokenMap = res.at("data", "nftToken").toMap(Value.class);
            Map<String, Boolean> nftToken = new HashMap<>();
            for (Map.Entry<String, Value> entry : nftTokenMap.entrySet()) {
                nftToken.put(entry.getKey(), entry.getValue().to(Boolean.class).get());
            }
            Double costValue = res.at("data", "cost").get(Value.DoubleV.class).to(Double.class).get();
            int cost = costValue.intValue();
            return new TicketResponse(
                    res.at("ref").to(Value.RefV.class).get().getId(),
                    res.at("data", "count").to(Integer.class).get(),
                    cost,
                    user,
                    tier,
                    event,
                    res.at("data", "transactionId").to(String.class).get(),
                    nftToken
            );
        }catch (Exception e){
            throw new ResourceNotFoundException("Ticket", "Id", id);
        }

    }

    @Override
    public List<TicketResponse> getTicketByUserId(String userId){
        try {
            CompletableFuture<Value> res = faunaClient.query(Paginate(Documents(Collection("Ticket"))));
            Value value = res.join();
            List<Value> valueList = value.at("data").collect(Value.class).stream().toList();
            List<TicketResponse> tickets = new ArrayList<>();
            for(Value val : valueList) {
                String ticketId = ((Value.RefV)val).getId();
                TicketResponse ticket = getTicketById(ticketId);
                if(ticket.user().id().equals(userId)) {
                    tickets.add(ticket);
                }
            }
            return tickets;

        }catch (Exception e){
            throw new ResourceNotFoundException("Ticket", "userId", userId);
        }
    }

    @Override
    public boolean deleteTicketById(String id) {
        try {
            getTicketById(id);
            try {
                Value val = faunaClient.query(Delete(Ref(Collection("Ticket"), id))).join();
                if (val == null) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }catch (Exception e){
            throw new ResourceNotFoundException("Ticket", "Id", id);
        }

    }
}
