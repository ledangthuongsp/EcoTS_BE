package com.example.EcoTS.Mappers;

import com.example.EcoTS.DTOs.Response.Donation.DonationResponseDTO;
import com.example.EcoTS.Models.Donations;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DonationMapper {
    @Mapping(source = "sponsor.id",            target = "sponsorId")
    @Mapping(source = "sponsor.companyName",   target = "sponsorName")
    DonationResponseDTO toDTO(Donations donation);
}