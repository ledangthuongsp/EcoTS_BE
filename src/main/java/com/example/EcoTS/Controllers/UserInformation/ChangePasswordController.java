package com.example.EcoTS.Controllers.UserInformation;

import com.example.EcoTS.DTOs.Request.User.ChangePasswordRequest;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.UserRepository;
import com.example.EcoTS.Services.SecurityService.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Controller
@RestController
@CrossOrigin
public class ChangePasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;  // Use BCryptPasswordEncoder

    @PostMapping("/user/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        String token = request.getToken();
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        try {
            String username = jwtService.getUsername(token);

            Optional<Users> optionalUser = userRepository.findByUsername(username);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            Users user = optionalUser.get();

            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Old password does not match.");
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return ResponseEntity.ok("Password successfully updated.");
        } catch (Exception e) {
            // Log the exception and provide a generic error message
            System.out.println("Error changing password: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("You need to login again - Your token has been expired");
        }
    }
}
