package com.example.EcoTS.Controllers.Auth.SignUp;

import com.example.EcoTS.DTOs.Request.Auth.SignUpDTO;
import com.example.EcoTS.Enum.Roles;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.LocationRepository;
import com.example.EcoTS.Services.SecurityService.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RestController
@CrossOrigin
@Tag(name = "Authentication", description = "Authentication APIs")
public class SignUpController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private LocationRepository locationRepository;

    @PostMapping("/auth/signup")
    public ResponseEntity<Users> register(@RequestBody SignUpDTO registerUserDto, @RequestParam (required = false) Roles roles,
                                          @RequestParam (required = false) Long locationId) {
        Users registeredUser = authenticationService.signup(registerUserDto, roles, locationId);
        return ResponseEntity.ok(registeredUser);
    }
}
