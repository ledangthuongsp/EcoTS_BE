package com.example.EcoTS.DTOs.Request.Newsfeed;

import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewsfeedStatistics {
    private Newsfeed mostReactedNewsfeed;
    private Newsfeed mostVotedNewsfeed;
    private int totalReacts; // Tổng số react
    private int totalVotes;  // Tổng số người tham gia (vote)
}
