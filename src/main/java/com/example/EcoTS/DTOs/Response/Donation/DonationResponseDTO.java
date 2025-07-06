package com.example.EcoTS.DTOs.Response.Donation;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DonationResponseDTO {
    private Long id;
    private String title;
    private String name;
    private String description;
    private List<String> coverImageUrl;
    private List<String> sponsorImages;
    private Timestamp startDate;
    private Timestamp endDate;
    private double totalDonations;
    private Long sponsorId;
    private String sponsorName;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
