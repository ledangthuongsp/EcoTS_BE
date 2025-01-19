package com.example.EcoTS.Models;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Entity
@Table(name = "sponsor_qr_code")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SponsorQRCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sponsor_id", nullable = false)
    private Long sponsorId;

    @Column(name = "qr_code_url", nullable = false)
    private String qrCodeUrl;

    @Column(name = "points", nullable = false)
    private Double points;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "expired_at", nullable = false)
    private Timestamp expiredAt;

    @Column(name = "is_used", nullable = false)
    private Boolean isUsed;  // TRUE nếu đã bị khóa hoặc hết hạn

    @Column(name = "newsfeed_id", nullable = false)
    private Long newsfeedId;
}
