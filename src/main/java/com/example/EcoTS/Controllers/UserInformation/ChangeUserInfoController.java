package com.example.EcoTS.Controllers.UserInformation;

import com.example.EcoTS.DTOs.Request.User.ChangeInfoRequest;
import com.example.EcoTS.DTOs.Request.User.EmployeeRequest;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.UserRepository;
import com.example.EcoTS.Services.SecurityService.JwtService;
import com.example.EcoTS.Services.UserService.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@Tag(name ="User Profile", description = "apis for changing user profile and information")
public class ChangeUserInfoController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @PutMapping("/user/change-info")
    public ResponseEntity<?> updateUserInfo(@RequestBody ChangeInfoRequest changeInfoRequest) throws Exception {
        String token = changeInfoRequest.getToken();
        String username = jwtService.getUsername(token);

        Optional<Users> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        Users user = optionalUser.get();

        // Cập nhật thông tin người dùng nếu các trường trong request được cung cấp
        if (changeInfoRequest.getFullName() != null) {
            user.setFullName(changeInfoRequest.getFullName());
        }
        if (changeInfoRequest.getDayOfBirth() != null) {
            user.setDayOfBirth(changeInfoRequest.getDayOfBirth());
        }
        if (changeInfoRequest.getGender() != null) {
            user.setGender(changeInfoRequest.getGender());
        }
        if (changeInfoRequest.getAddress() != null) {
            user.setAddress(changeInfoRequest.getAddress());
        }
        if (changeInfoRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(changeInfoRequest.getPhoneNumber());
        }
        if (changeInfoRequest.getPersonalId() != null) {
            user.setPersonalId(changeInfoRequest.getPersonalId());
        }

        // Lưu thông tin người dùng đã được cập nhật
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("Your information has been updated successfully !");
    }
    @PutMapping("/employee/change-info")
    public ResponseEntity<?> updateEmployeeInfo(@RequestBody EmployeeRequest employeeRequest, @RequestParam Long employeeId) throws Exception {


        Users user = userRepository.findById(employeeId).orElseThrow();

        // Cập nhật thông tin người dùng nếu các trường trong request được cung cấp
        if (employeeRequest.getFullName() != null) {
            user.setFullName(employeeRequest.getFullName());
        }
        if (employeeRequest.getDayOfBirth() != null) {
            user.setDayOfBirth(employeeRequest.getDayOfBirth());
        }
        if (employeeRequest.getGender() != null) {
            user.setGender(employeeRequest.getGender());
        }
        if (employeeRequest.getAddress() != null) {
            user.setAddress(employeeRequest.getAddress());
        }
        if (employeeRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(employeeRequest.getPhoneNumber());
        }
        if (employeeRequest.getPersonalId() != null) {
            user.setPersonalId(employeeRequest.getPersonalId());
        }

        // Lưu thông tin người dùng đã được cập nhật
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("Your information has been updated successfully !");
    }
}
