package com.example.EcoTS.Services.Newsfeed;

import com.example.EcoTS.DTOs.Request.Newsfeed.NewsfeedStatistics;
import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Repositories.Newsfeed.NewsfeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsfeedStatisticService {

    @Autowired
    private NewsfeedRepository newsfeedRepository;

    // Lấy tất cả bài viết của một sponsorId
    public List<Newsfeed> getAllNewsfeedBySponsorId(long sponsorId) {
        return newsfeedRepository.findBySponsorId(sponsorId);
    }

    // Lấy thống kê bài viết (react nhiều nhất, vote nhiều nhất) + tổng số người tham gia và react
    public NewsfeedStatistics getNewsfeedStatisticsBySponsorId(long sponsorId) {
        List<Newsfeed> newsfeeds = newsfeedRepository.findBySponsorId(sponsorId);

        // Tính tổng số react và số vote cho từng bài viết
        Map<Long, Integer> reactCountMap = new HashMap<>();
        Map<Long, Integer> voteCountMap = new HashMap<>();

        int totalReacts = 0;
        int totalVotes = 0;

        for (Newsfeed newsfeed : newsfeeds) {
            // Tính số react cho mỗi bài viết
            int reactCount = newsfeed.getReactIds().size();
            reactCountMap.put(newsfeed.getId(), reactCount);
            totalReacts += reactCount;

            // Tính số vote cho mỗi bài viết
            if (newsfeed.getPollId() != null) {
                voteCountMap.put(newsfeed.getId(), 1); // Giả sử mỗi pollId đại diện cho một vote duy nhất
                totalVotes += 1;
            }
        }

        // Lấy bài viết có react nhiều nhất và vote nhiều nhất
        Newsfeed mostReactedNewsfeed = getMostReactedNewsfeed(newsfeeds);
        Newsfeed mostVotedNewsfeed = getMostVotedNewsfeed(newsfeeds);

        return new NewsfeedStatistics(mostReactedNewsfeed, mostVotedNewsfeed, totalReacts, totalVotes);
    }

    // Phương thức để lấy bài viết có nhiều react nhất
    public Newsfeed getMostReactedNewsfeed(List<Newsfeed> newsfeeds) {
        return newsfeeds.stream()
                .max(Comparator.comparingInt(newsfeed -> newsfeed.getReactIds().size()))
                .orElse(null);
    }

    // Phương thức để lấy bài viết có nhiều vote nhất
    public Newsfeed getMostVotedNewsfeed(List<Newsfeed> newsfeeds) {
        return newsfeeds.stream()
                .filter(newsfeed -> newsfeed.getPollId() != null)
                .max(Comparator.comparingInt(newsfeed -> 1)) // Lấy những bài có pollId
                .orElse(null);
    }
}
