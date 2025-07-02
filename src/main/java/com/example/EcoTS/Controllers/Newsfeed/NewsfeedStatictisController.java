package com.example.EcoTS.Controllers.Newsfeed;

import com.example.EcoTS.DTOs.Request.Newsfeed.NewsfeedStatistics;
import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Services.Newsfeed.NewsfeedStatisticService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/newsfeed-statistic")
@Tag(name = "Newsfeed Statistic")
public class NewsfeedStatictisController {

    @Autowired
    private NewsfeedStatisticService newsfeedStatisticService;

    // API lấy tất cả bài viết của sponsor
    @GetMapping("/get-all")
    public ResponseEntity<List<Newsfeed>> getAllNewsfeed(@RequestParam long sponsor_id) {
        return ResponseEntity.ok(newsfeedStatisticService.getAllNewsfeedBySponsorId(sponsor_id));
    }

    // API lấy thống kê tổng quan (tổng react, total vote, most reacted, most voted)
    @GetMapping("/get-statistics")
    public ResponseEntity<NewsfeedStatistics> getStatistics(@RequestParam long sponsor_id) {
        return ResponseEntity.ok(newsfeedStatisticService.getNewsfeedStatisticsBySponsorId(sponsor_id));
    }

    // API lấy bài viết có react nhiều nhất
    @GetMapping("/most-reacted")
    public ResponseEntity<Newsfeed> getMostReactedNewsfeed(@RequestParam long sponsor_id) {
        List<Newsfeed> newsfeeds = newsfeedStatisticService.getAllNewsfeedBySponsorId(sponsor_id);
        Newsfeed mostReacted = newsfeedStatisticService.getMostReactedNewsfeed(newsfeeds);
        return ResponseEntity.ok(mostReacted);
    }

    // API lấy bài viết có vote nhiều nhất
    @GetMapping("/most-voted")
    public ResponseEntity<Newsfeed> getMostVotedNewsfeed(@RequestParam long sponsor_id) {
        List<Newsfeed> newsfeeds = newsfeedStatisticService.getAllNewsfeedBySponsorId(sponsor_id);
        Newsfeed mostVoted = newsfeedStatisticService.getMostVotedNewsfeed(newsfeeds);
        return ResponseEntity.ok(mostVoted);
    }
}
