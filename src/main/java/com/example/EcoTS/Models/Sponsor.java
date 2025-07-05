package com.example.EcoTS.Models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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

    @OneToMany(mappedBy = "sponsor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Locations> locations;

    @OneToMany(mappedBy = "sponsor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Donations> donations;
}
