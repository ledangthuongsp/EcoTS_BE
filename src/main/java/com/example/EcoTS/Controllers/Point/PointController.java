package com.example.EcoTS.Controllers.Point;

import com.example.EcoTS.Repositories.PointRepository;
import com.example.EcoTS.Repositories.UserRepository;
import com.example.EcoTS.Services.SecurityService.JwtService;
import com.example.EcoTS.Services.UserService.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

@RestController
@CrossOrigin
@Tag(name ="Points CRUD", description = "this apis for changing and updating, adding points for users")
public class PointController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;
}
