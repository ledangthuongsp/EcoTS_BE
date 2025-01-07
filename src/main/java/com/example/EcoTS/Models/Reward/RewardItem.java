package com.example.EcoTS.Models.Reward;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@Entity
@Table(name = "reward_item")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class RewardItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private List<String> rewardItemUrl;
    private double pointCharge;
    private int stock;
    private String itemName;
    private String itemDescription;
    private List<Long> itemType;

    private double height;
    private double humidity;
    private String size;
    private String weight;
}
