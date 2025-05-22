package com.example.EcoTS.Models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "sponsor")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Sponsor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
