package com.example.EcoTS.Models.Reward;

import com.example.EcoTS.Enum.RewardOrderStatus;
import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Users;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "reward_order")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_item_id")
    private RewardItem rewardItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Locations location;

    @Enumerated(EnumType.STRING)
    private RewardOrderStatus status;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}

