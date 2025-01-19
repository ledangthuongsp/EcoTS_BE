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
    private Long sponsorId;  // ID của sponsor tạo QR code

    @Column(name = "qr_code_url", nullable = false)
    private String qrCodeUrl;  // URL của QR code đã tạo

    @Column(name = "points", nullable = false)
    private Double points;  // Điểm mà người quét sẽ nhận

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;  // Thời gian tạo QR code

    @Column(name = "expired_at", nullable = false)
    private Timestamp expiredAt;  // Thời gian hết hạn của QR code (sau 3 phút)

    @Column(name = "is_used", nullable = false)
    private Boolean isUsed;  // Trạng thái xem QR đã được sử dụng hay chưa

    @Column(name ="newsfeed_id", nullable = false)
    private Long newsfeedId;

    @Column(name = "user_email", nullable = true)
    private String userEmail;  // Email của người quét QR nếu cần (có thể lưu lại sau khi người dùng quét)

    @Column(name = "proof_image_url", nullable = true)
    private String proofImageUrl;  // URL của ảnh minh chứng (được upload sau khi quét QR)
}
