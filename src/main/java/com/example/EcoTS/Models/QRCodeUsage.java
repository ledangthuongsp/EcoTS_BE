package com.example.EcoTS.Models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Entity
@Table(name = "qr_code_usage")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QRCodeUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "qr_code_id", nullable = false)
    private SponsorQRCode sponsorQRCode;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "proof_image_url", nullable = true)
    private String proofImageUrl;

    @Column(name = "used_at", nullable = false)
    private Timestamp usedAt;
}
