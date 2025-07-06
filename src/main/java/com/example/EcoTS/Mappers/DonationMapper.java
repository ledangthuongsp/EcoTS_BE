package com.example.EcoTS.Mappers;

import com.example.EcoTS.DTOs.Response.Donation.DonationResponseDTO;
import com.example.EcoTS.Models.Donations;
import org.springframework.stereotype.Component;

@Component
public class DonationMapper {

    public DonationResponseDTO toDTO(Donations donation) {
        DonationResponseDTO dto = new DonationResponseDTO();
        dto.setId(donation.getId());
        dto.setTitle(donation.getTitle());
        dto.setName(donation.getName());
        dto.setDescription(donation.getDescription());
        dto.setCoverImageUrl(donation.getCoverImageUrl());
        dto.setSponsorImages(donation.getSponsorImages());
        dto.setStartDate(donation.getStartDate());
        dto.setEndDate(donation.getEndDate());
        dto.setTotalDonations(donation.getTotalDonations());
        if (donation.getSponsor() != null) {
            dto.setSponsorId(donation.getSponsor().getId());
            dto.setSponsorName(donation.getSponsor().getCompanyName());
        }
        dto.setCreatedAt(donation.getCreatedAt());
        dto.setUpdatedAt(donation.getUpdatedAt());
        return dto;
    }
}
