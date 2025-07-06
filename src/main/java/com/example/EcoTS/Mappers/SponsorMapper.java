// src/main/java/com/example/EcoTS/Mappers/SponsorMapper.java

package com.example.EcoTS.Mappers;

import com.example.EcoTS.DTOs.Response.Sponsor.SponsorResponse;
import com.example.EcoTS.Models.Sponsor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SponsorMapper {
    public SponsorResponse toDTO(Sponsor s) {
        List<Long> locIds = s.getLocations() == null
                ? Collections.emptyList()
                : s.getLocations().stream().map(loc -> loc.getId()).toList();

        List<Long> donIds = s.getDonations() == null
                ? Collections.emptyList()
                : s.getDonations().stream().map(d -> d.getId()).toList();

        return SponsorResponse.builder()
                .id(s.getId())
                .companyUsername(s.getCompanyUsername())
                .avatarUrl(s.getAvatarUrl())
                .companyName(s.getCompanyName())
                .companyPhoneNumberContact(s.getCompanyPhoneNumberContact())
                .companyEmailContact(s.getCompanyEmailContact())
                .companyAddress(s.getCompanyAddress())
                .businessDescription(s.getBusinessDescription())
                .companyDirectorName(s.getCompanyDirectorName())
                .companyTaxNumber(s.getCompanyTaxNumber())
                .companyPoints(s.getCompanyPoints())
                .locationIds(locIds)
                .donationIds(donIds)
                .build();
    }
}
