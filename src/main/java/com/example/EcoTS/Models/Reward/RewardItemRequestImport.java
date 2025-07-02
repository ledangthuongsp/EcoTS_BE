package com.example.EcoTS.Models.Reward;

import com.example.EcoTS.Enum.ImportStatus;
import com.example.EcoTS.Models.Locations;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@Entity
@Table(name = "reward_item_request_import")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class RewardItemRequestImport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    ImportStatus importStatus; // WAITING, IMPORTING, CONFIRMED, CANCELLED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    Locations location;

    @OneToMany(mappedBy = "requestImport", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<RewardItemRequestImportDetail> items;

    @CreationTimestamp
    Timestamp createdAt;

    @UpdateTimestamp
    Timestamp updatedAt;
}
