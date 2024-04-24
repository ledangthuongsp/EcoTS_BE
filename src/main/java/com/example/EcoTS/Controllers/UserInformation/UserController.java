package com.example.EcoTS.Controllers.UserInformation;

import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Services.SecurityService.JwtService;
import com.example.EcoTS.Services.UserService.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @GetMapping("/username") // Endpoint to get a user by username
    @Operation(summary = "Get user by username", description = "Retrieve a user profile by their username")
    public ResponseEntity<Users> getUserByUsername(@RequestParam("username") String username) {
        Users user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
    @GetMapping("/token")
    public ResponseEntity<Users> getUserInfo(@RequestParam("token") String token) {

        String username = jwtService.getUsername(token);

        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Users user = userService.getUserByUsername(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }
    @GetMapping("/get-all-users")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.getAllUsers();

        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
