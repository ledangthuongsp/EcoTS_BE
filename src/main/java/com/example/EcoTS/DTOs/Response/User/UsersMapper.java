package com.example.EcoTS.DTOs.Response.User;

import com.example.EcoTS.Models.Users;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {
    public UsersDTO toDTO(Users user) {
        UsersDTO dto = new UsersDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setPersonalId(user.getPersonalId());
        dto.setDayOfBirth(user.getDayOfBirth());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setGender(user.getGender());
        dto.setRole(user.getRole());
        return dto;
    }
}
