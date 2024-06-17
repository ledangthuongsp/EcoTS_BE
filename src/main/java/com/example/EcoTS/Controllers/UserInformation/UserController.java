package com.example.EcoTS.Controllers.UserInformation;

import com.example.EcoTS.DTOs.Response.User.UserResponse;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.UserRepository;
import com.example.EcoTS.Services.Achievement.AchievementService;
import com.example.EcoTS.Services.SecurityService.JwtService;
import com.example.EcoTS.Services.UserService.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@RequestMapping("/user")
@Tag(name ="User Profile", description = "apis for changing user profile and information")
public class UserController {
    @Autowired
    private UserService userService; // Service to handle user retrieval and management
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/username") // Endpoint to get a user by username
    @Operation(summary = "Get user by username", description = "Retrieve a user profile by their username")
    public ResponseEntity<UserResponse> getUserByUsername(@RequestParam("username") String username) {
        Users user = userService.getUserByUsername(username);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(username);
        userResponse.setEmail(user.getEmail());
        userResponse.setFullName(user.getFullName());
        userResponse.setRole(user.getRole());
        userResponse.setAvatarUrl(user.getAvatarUrl());
        userResponse.setDayOfBirth(user.getDayOfBirth());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setAddress(user.getAddress());
        return ResponseEntity.ok(userResponse);
    }
    @GetMapping("/token")
    public ResponseEntity<UserResponse> getUserInfo(@RequestParam("token") String token) {

        String username = jwtService.getUsername(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Users user = userService.getUserByUsername(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(username);
        userResponse.setEmail(user.getEmail());
        userResponse.setFullName(user.getFullName());
        userResponse.setRole(user.getRole());
        userResponse.setAvatarUrl(user.getAvatarUrl());
        userResponse.setDayOfBirth(user.getDayOfBirth());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setAddress(user.getAddress());
        return ResponseEntity.ok(userResponse);
    }
    @GetMapping("/get-all-users")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @DeleteMapping("/delete-user-by-id")
    public ResponseEntity<?> deleteUserById(@RequestParam Long id, @RequestParam String username) {
        Optional<Users> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        Users user = optionalUser.get();
        if (!user.getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username does not match the provided ID.");
        }

        try {
            userService.deleteUser(user);
            return ResponseEntity.ok().body("User deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user.");
        }
    }
}
