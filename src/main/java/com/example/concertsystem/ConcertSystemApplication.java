package com.example.concertsystem;

import com.example.concertsystem.entity.User;
import com.example.concertsystem.service.user.UserService;
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
	public CommandLineRunner commandLineRunner(UserService userService) {
		return runner-> {
//			createNewUser(userService);
//			getUserByUserId(userService);
//			deleteUserById(userService);
//			updateUserInfoById(userService);
//			updateUserRoleById(userService);
			getUsersByRole(userService);
		};
	}

	private void getUsersByRole(UserService userService) {
		String role = "user";
		List<User> userList = userService.getUsersByType(role);
		System.out.println(userList);
	}

	private void updateUserRoleById(UserService userService) throws ExecutionException, InterruptedException {
		String userId = "386520479936020544";
		userService.updateUserRole(userId, "admin");
		System.out.println("User updated successfully!!");
	}

	private void updateUserInfoById(UserService userService) throws ExecutionException, InterruptedException {
		String userId = "386520479936020544";
		userService.updateUserInfo(userId, "def");
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
