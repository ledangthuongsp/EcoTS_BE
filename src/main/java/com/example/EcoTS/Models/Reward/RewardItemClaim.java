package com.example.EcoTS.Models.Reward;

import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Enum.RewardItemClaimStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "reward_item_claim")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RewardItemClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_item_id", nullable = false)
    RewardItem rewardItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    Locations location;

    @Enumerated(EnumType.STRING)
    RewardItemClaimStatus status;

    @CreationTimestamp
    Timestamp createdAt;

    @UpdateTimestamp
    Timestamp updatedAt;
}
