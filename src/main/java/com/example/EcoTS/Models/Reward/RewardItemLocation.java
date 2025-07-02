package com.example.EcoTS.Models.Reward;

import com.example.EcoTS.Models.Locations;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
@Builder
@Getter
@Setter
@Entity
@Table(name = "reward_item_location")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class RewardItemLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Locations location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_item_id", referencedColumnName = "id")
    private RewardItem rewardItem;

    @Builder.Default
    @Column(nullable = false)
    private Long importing = 0L;
    @Builder.Default
    @Column(nullable = false)
    private Long stock = 0L;
    @Builder.Default
    @Column(nullable = false)
    private Long pending = 0L;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
