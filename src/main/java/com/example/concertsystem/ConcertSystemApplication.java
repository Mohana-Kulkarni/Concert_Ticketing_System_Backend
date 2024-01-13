package com.example.concertsystem;

import com.example.concertsystem.entity.Place;
import com.example.concertsystem.entity.Tier;
import com.example.concertsystem.entity.User;
import com.example.concertsystem.entity.Venue;
import com.example.concertsystem.service.place.PlaceService;
import com.example.concertsystem.service.tier.TierService;
import com.example.concertsystem.service.user.UserService;
import com.example.concertsystem.service.venue.VenueService;
import com.faunadb.client.types.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class ConcertSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcertSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(VenueService venueService) {
		return runner-> {
//			createNewUser(userService);
//			getUserByUserId(userService);
//			deleteUserById(userService);
//			updateUserInfoById(userService);
//			updateUserRoleById(userService);
//			getUsersByRole(userService);

//			addNewPlace(placeService);
//			getPlaceWithName(placeService);
//			getPlaceWithId(placeService);
//			updatePlaceName(placeService);
//			deletePlace(placeService);

//			addNewVenue(venueService);
//			getVenueWithId(venueService);
			getVenuesWithPlace(venueService);
//			updateVenue(venueService);
//			deleteVenue(venueService);

//			addNewTier(tierService);
//			getTierWithId(tierService);
//			getTierWithName(tierService);
//			updateTierInfo(tierService);
//			deleteTier(tierService);
		};
	}

//	private void deleteTier(TierService tierService) {
//		String id = "386687425602125890";
//		tierService.deleteTierById(id);
//		System.out.println("Tier deleted successfully!!!");
//	}
//
//	private void updateTierInfo(TierService tierService) throws ExecutionException, InterruptedException {
//		String id = "386687425602125890";
//		String name = "Platinum";
//		int capacity = 2000;
//		int price = 10000;
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
//	private void addNewTier(TierService tierService) {
//		String name = "Platinum";
//		int capacity = 3000;
//		int price = 10000;
//		tierService.addTier(name,capacity, price);
//		System.out.println("Tier added successfully!!!");
//
//	}
//
	private void getVenuesWithPlace(VenueService venueService) throws ExecutionException, InterruptedException {
		String city = "Mumbai";
		List<Venue> venueList = venueService.getVenuesByPlace(city);
		System.out.println(venueList);
	}

//	private void updateVenue(VenueService venueService) throws ExecutionException, InterruptedException {
//		String id = "386542879951028288";
//		String name = "Amnora";
//		String address = "Hadapsar";
//		int capacity = 10000;
//		String placeId = "386542192727949378";
//		venueService.updateVenueById(id, name, address, capacity, placeId);
//		System.out.println("Venue updated successfully!!!");
//	}
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

	private void getUsersByRole(UserService userService) {
		String role = "user";
		List<User> userList = userService.getUsersByType(role);
		System.out.println(userList);
	}

	private void updateUserRoleById(UserService userService) throws ExecutionException, InterruptedException {
		String userId = "386520479936020544";
		userService.updateUserRole(userId, "pqr","admin");
		System.out.println("User updated successfully!!");
	}

	private void updateUserInfoById(UserService userService) throws ExecutionException, InterruptedException {
		String userId = "386521241440223298";
		userService.updateUserInfo(userId, "xyz", "role");
		System.out.println("User updated successfully!!");

	}

	private void deleteUserById(UserService userService) {
		String userId = "386517276693626946";
		userService.deleteUser(userId);
		System.out.println("User deleted successfully!!");
	}

	private void getUserByUserId(UserService userService) throws ExecutionException, InterruptedException {
		String userId = "386517276693626946";
		User user = userService.getUserById(userId);
		System.out.println(user);
	}

	private void createNewUser(UserService userService) {
		String name = "ghi";
		String role = "user";
		userService.addUser(name, role);
		System.out.println("User added successfully!!");
	}

}
