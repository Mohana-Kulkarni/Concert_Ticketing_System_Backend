package com.example.concertsystem;

import com.example.concertsystem.dto.EventResponse;
import com.example.concertsystem.entity.*;
import com.example.concertsystem.service.event.EventService;
import com.example.concertsystem.service.place.PlaceService;
import com.example.concertsystem.service.tickets.TicketService;
import com.example.concertsystem.service.tier.TierService;
import com.example.concertsystem.service.user.UserService;
import com.example.concertsystem.service.venue.VenueService;
import com.faunadb.client.types.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@EnableCaching
public class ConcertSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcertSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(EventService eventService) {
		return runner-> {
//			createNewUser(userService);
//			getUserByUserId(userService);
//			deleteUserById(userService);
//			updateUserInfoById(userService);
//			updateUserRoleById(userService);
//			getUsersByRole(userService);
//			userByUserId(userService);

//			addNewPlace(placeService);
//			getPlaceWithName(placeService);
//			getPlaceWithId(placeService);
//			updatePlaceName(placeService);
//			deletePlace(placeService);

//			addNewVenue(venueService);
//			getVenueWithId(venueService);
//			getVenuesWithPlace(venueService);
//			updateVenue(venueService);
//			deleteVenue(venueService);

//			addNewTier(tierService);
//			getTierWithId(tierService);
//			getTierWithName(tierService);
//			updateTierInfo(tierService);
//			deleteTier(tierService);
//			getTierByNameEvent(tierService);

//			addNewEvent(eventService);
//			updateEvent(eventService);
//			eventByArtist(eventService);
//			getEventWithId(eventService);
//			getEventWithVenue(eventService);
//			getEventWithPlace(eventService);
//			getAllEventsList(eventService);
//			getPlaceByEventId(eventService);


//			buyTicket(ticketService, tierService);
//			getTicketWithId(ticketService);
//			getTicketWithUserName(ticketService);
//			updateTicketWithId(ticketService);


		};
	}

//	private void getTierByNameEvent(TierService tierService) throws ExecutionException, InterruptedException {
//		String name = "Platinum";
//		String eventId = "387233174551265344";
//		Tier tier = tierService.getTierByEventId(eventId);
//		System.out.println(tier);
//	}

//	private void getAllEventsList(EventService eventService) throws ExecutionException, InterruptedException {
//		eventService.getAllEvents();
//	}

//	private void updateTicketWithId(TicketService ticketService) throws ExecutionException, InterruptedException {
//		String id = "386884281674235970";
//		String userName = "user@123";
//		String tierName = "Gold";
//		String eventName = "Spring Holiday Concert";
//		int count = 3;
//		ticketService.updateTicket(id, count, userName, tierName, eventName);
//		System.out.println("Ticket updated successfully!!!");
//	}

//	private void getTicketWithUserName(TicketService ticketService) throws ExecutionException, InterruptedException {
//		String userName = "user@123";
//		System.out.println(ticketService.getTicketByUserName(userName));
//	}

//	private void getTicketWithId(TicketService ticketService) throws ExecutionException, InterruptedException {
//		String id = "386884281674235970";
//		System.out.println(ticketService.getTicketById(id));
//	}

	private void getPlaceByEventId(EventService eventService) throws ExecutionException, InterruptedException {
		System.out.println(eventService.getPlaceByEventId("387425161922478146"));
	}

//	private void buyTicket(TicketService ticketService, TierService tierService) throws ExecutionException, InterruptedException {
//		String userName = "user@123";
//		String tierName = "Gold";
//		String eventName = "Winter Holiday Concert";
//		int count = 3;
//
//		ticketService.generateTicket(count, userName, tierName, eventName);
//		System.out.println("Ticket Booked Successfully!!!");
//	}

//	private void addNewEvent(EventService eventService) throws ExecutionException, InterruptedException {
//		String name = "Summer Concert";
//		DateTimeFormatter formatter
//				= DateTimeFormatter.ofPattern(
//				"yyyy-MM-dd HH:mm:ss a");
//		LocalDateTime now = LocalDateTime.now();
//		String dateTimeString = now.format(formatter);
//		String description = "It is a Hot concert Summer event. Free Wine and Pizza";
//		String venue = "The Millers";
//
//		List<String> username = new ArrayList<>();
//		username.add("user@123");
//		List<String> tiername = new ArrayList<>();
//		tiername.add("Platinum");
//		eventService.addEvent(name, dateTimeString, description, venue, username, tiername);
//		System.out.println("Event Added Successfully");
//
//
//	}
//	private void updateEvent(EventService eventService) throws ExecutionException, InterruptedException {
//		String name = "Spring Holiday Concert";
//		DateTimeFormatter formatter
//				= DateTimeFormatter.ofPattern(
//				"yyyy-MM-dd HH:mm:ss a");
//		LocalDateTime now = LocalDateTime.now();
//		String dateTimeString = now.format(formatter);
//		String description = "It is a cold concert winter event. Free Beer(Draught)";
//		String venue = "Phoenix";
//
//		List<String> username = new ArrayList<>();
//		username.add("user@123");
//		username.add("user@321");
//		List<String> tiername = new ArrayList<>();
//		tiername.add("Platinum");
//		eventService.updateEvent("386837366771286080",name,dateTimeString,description,venue,username,tiername);
//		System.out.println("Event updated successfully!!!");
//	}

//	private void eventByArtist(EventService eventService) throws ExecutionException, InterruptedException {
//		String artist = "abc";
//		List<Event> events = eventService.getEventByArtist(artist);
//		for(Event event : events){
//			System.out.println(event);
//		}
//	}
//
//	private void getEventWithId(EventService eventService) throws ExecutionException, InterruptedException {
//		String id = "387191950416019522";
//		EventResponse event = eventService.getEventById(id);
//		System.out.println(event);
//
//	}
//
//	private void getEventWithVenue(EventService eventService) throws ExecutionException, InterruptedException {
//		String place = "Phoenix";
//		List<Event> event = eventService.getEventByVenue(place);
//		System.out.println(event);
//	}
//
//	private void getEventWithPlace(EventService eventService) throws ExecutionException, InterruptedException {
//		String place = "Nashik";
//		List<Event> events = eventService.getEventByPlace(place);
//		System.out.println(events);
//
//	}

//	private void deleteTier(TierService tierService) {
//		String id = "386687425602125890";
//		tierService.deleteTierById(id);
//		System.out.println("Tier deleted successfully!!!");
//	}
//
//	private void updateTierInfo(TierService tierService) throws ExecutionException, InterruptedException {
//		String id = "386869956857299010";
//		String name = "Gold";
//		int capacity = 4998;
//		int price = 6000;
//		tierService.updateTier(id,name,capacity, price);
//		System.out.println("Tier updated successfully!!!");
//	}
//
//	private void getTierWithName(TierService tierService) throws ExecutionException, InterruptedException {
//		String name = "VIP";
//		Tier tier = tierService.getTierByName(name);
//		System.out.println(tier);
//	}
//
//	private void getTierWithId(TierService tierService) throws ExecutionException, InterruptedException {
//		String id = "386687425602125890";
//		Tier tier = tierService.getTierById(id);
//		System.out.println(tier);
//
//	}
//
//	private void addNewTier(TierService tierService) throws ExecutionException, InterruptedException {
//		String name = "Silver";
//		int capacity = 5000;
//		int price = 5000;
//		tierService.addTier(name,capacity, price);
//		System.out.println("Tier added successfully!!!");
//
//	}
//
	private void getVenuesWithPlace(VenueService venueService) throws ExecutionException, InterruptedException {
		String city = "Mumbai";
		List<Venue> venueList = venueService.getVenueByPlace(city);
		System.out.println(venueList);
	}

	private void updateVenue(VenueService venueService) throws ExecutionException, InterruptedException {
		String id = "386544741426135104";
		String name = "The Millers";
		String address = "Station Road";
		int capacity = 10000;
		String placeId = "386704395924930624";
		venueService.updateVenueById(id, name, address, capacity, placeId);
		System.out.println("Venue updated successfully!!!");
	}
//
//	private void getVenueWithId(VenueService venueService) throws ExecutionException, InterruptedException {
//		String id = "386542879951028288";
//		Venue venue = venueService.getVenueById(id);
//		System.out.println(venue);
//	}
//
//	private void addNewVenue(VenueService venueService) {
//		String name = "The Millers";
//		String address = "Station Road";
//		int capacity = 13000;
//		String placeId = "386521205301051456";
//
//		Venue venue = new Venue(name, address, capacity, placeId);
//		venueService.addVenue(venue);
//		System.out.println("Venue added successfully!!!");
//
//	}
//
//	private void deletePlace(PlaceService placeService) {
//		String id = "386527844088414274";
//		placeService.deletePlaceById(id);
//		System.out.println("Place deleted successfully!!!");
//	}
//
//	private void updatePlaceName(PlaceService placeService) throws ExecutionException, InterruptedException {
//		String id = "386527844088414274";
//		String city = "Pune";
//		placeService.updatePlaceById(id, city);
//		System.out.println("Place updated successfully!!!");
//	}
//
//	private void getPlaceWithName(PlaceService placeService) throws ExecutionException, InterruptedException {
//		String city = "Mumbai";
//		Place place = placeService.getPlaceByName(city);
//		System.out.println(place);
//	}
//
//	private void getPlaceWithId(PlaceService placeService) throws ExecutionException, InterruptedException {
//		String id = "386527844088414274";
//		Place place = placeService.getPlaceById(id);
//		System.out.println(place);
//	}
//
//	private void addNewPlace(PlaceService placeService) {
//		String city = "Pune";
//		placeService.addPlace(city);
//		System.out.println("Place added successfully!!!");
//	}

//	private void getUsersByRole(UserService userService) {
//		String role = "user";
//		List<User> userList = userService.getUsersByType(role);
//		System.out.println(userList);
//	}

//	private void updateUserRoleById(UserService userService) throws ExecutionException, InterruptedException {
//		String userId = "386814883372466242";
//		userService.updateUserRole(userId, "abc","artist");
//		System.out.println("User updated successfully!!");
//	}

	private void userByUserId(UserService userService){
		List<String> list = new ArrayList<>();
		list.add("user@123");
		list.add("user@321");
		System.out.println(userService.getUserIdsByUserName(list));
	}

//	private void updateUserInfoById(UserService userService) throws ExecutionException, InterruptedException {
//		String userId = "386521241440223298";
//		userService.updateUserInfo(userId, "xyz", "role");
//		System.out.println("User updated successfully!!");
//
//	}

	private void deleteUserById(UserService userService) {
		String userId = "386517276693626946";
		userService.deleteUser(userId);
		System.out.println("User deleted successfully!!");
	}

//	private void getUserByUserId(UserService userService) throws ExecutionException, InterruptedException {
//		String userId = "386517276693626946";
//		User user = userService.getUserById(userId);
//		System.out.println(user);
//	}

//	private void createNewUser(UserService userService) {
//		String name = "abc";
//		String role = "user";
//		String userName = "user@321";
//		String profileImg = "user@gmail.com";
//		userService.addUser(name, role, userName, profileImg);
//		System.out.println("User added successfully!!");
//	}

}
