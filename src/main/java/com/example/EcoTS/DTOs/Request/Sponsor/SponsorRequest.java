package com.example.EcoTS.DTOs.Request.Sponsor;

import lombok.Builder;

@Builder
public class SponsorRequest {
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
}
