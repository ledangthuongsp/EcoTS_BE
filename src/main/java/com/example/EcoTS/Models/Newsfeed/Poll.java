package com.example.EcoTS.Models.Newsfeed;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "poll")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "newsfeed_id", nullable = false)
    Newsfeed newsfeed; // Bài viết chứa poll

    @ElementCollection
    List<String> options; // Danh sách các lựa chọn trong poll

    @Column(name = "end_time", nullable = false)
    LocalDateTime endTime; // Thời gian kết thúc poll

    @Column(name = "total_votes")
    int totalVotes; // Tổng số lượt bình chọn
}
