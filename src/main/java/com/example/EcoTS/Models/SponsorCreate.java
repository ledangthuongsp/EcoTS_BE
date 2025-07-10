package com.example.EcoTS.Models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "sponsor_create")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class SponsorCreate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String natureOfBusiness;
    private String address;
    private String contactName;
    private String postcode;
    private String contactPhone;
    private String email;
    private String taxNumber;
    private String idea;  // Làm rõ thông tin ý tưởng

    private String additionalFileUrl;  // URL lưu trữ file PDF được upload

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;  // Trạng thái đăng ký, mặc định là PENDING

    public enum Status {
        PENDING,
        CONFIRMED
    }
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
