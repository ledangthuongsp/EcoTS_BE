package com.example.EcoTS.DTOs.Response.Sponsor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SponsorResponse {
    private Long id;
    private String companyUsername;
    private String companyPassword;
    private String avatarUrl;
    private String companyName;
    private String companyPhoneNumberContact;
    private String companyEmailContact;
    private String companyAddress;
    private String businessDescription;
    private String companyDirectorName;
    private String companyTaxNumber;
    private double companyPoints;
    private List<Long> locationIds;
    private List<Long> donationIds;
}
