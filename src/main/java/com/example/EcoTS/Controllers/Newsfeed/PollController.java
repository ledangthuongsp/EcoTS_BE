package com.example.EcoTS.Controllers.Newsfeed;

import com.example.EcoTS.DTOs.Response.Newsfeed.PollOptionResponse;
import com.example.EcoTS.DTOs.Response.Newsfeed.PollResponse;
import com.example.EcoTS.DTOs.Response.Newsfeed.VoteResponse;
import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Models.Newsfeed.Poll;
import com.example.EcoTS.Models.Newsfeed.PollOption;
import com.example.EcoTS.Models.Newsfeed.Vote;
import com.example.EcoTS.Repositories.Newsfeed.NewsfeedRepository;
import com.example.EcoTS.Repositories.Newsfeed.PollRepository;
import com.example.EcoTS.Services.Newsfeed.PollService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/poll")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Poll")
public class PollController {
    @Autowired
    private PollService pollService;
    @Autowired
    private NewsfeedRepository newsfeedRepository;
    @Autowired
    private PollRepository pollRepository;

    @PostMapping("/{newsfeedId}/{pollOptionId}/votes")
    public ResponseEntity<PollOption> addVote(
            @PathVariable Long newsfeedId,
            @PathVariable Long pollOptionId,
            @RequestParam Long userId,
            @RequestParam boolean status) {
        PollOption updatedPollOption = pollService.addVote(newsfeedId, pollOptionId, userId, status);
        return ResponseEntity.ok(updatedPollOption);
    }

    @DeleteMapping("/{newsfeedId}/{pollOptionId}/votes/{voteId}")
    public ResponseEntity<PollOption> removeVote(
            @PathVariable Long newsfeedId,
            @PathVariable Long pollOptionId,
            @PathVariable Long voteId) {
        PollOption updatedPollOption = pollService.removeVote(newsfeedId, pollOptionId, voteId);
        return ResponseEntity.ok(updatedPollOption);
    }
    @GetMapping("/by-newsfeed/{newsfeedId}")
    public ResponseEntity<PollResponse> getPollByNewsfeedId(@PathVariable Long newsfeedId) {
        PollResponse pollResponse = pollService.getPollByNewsfeedId(newsfeedId);
//        List<PollOptionResponse> pollOptionResponse = pollResponse.getPollOptions();
//        List<VoteResponse> voteResponses = pollOptionResponse.
        return ResponseEntity.ok(pollResponse);
    }
    @GetMapping("/get-vote-id")
    public ResponseEntity<?> getVoteIdByUserId(@RequestParam Long userId, @RequestParam Long newsfeedId)
    {
        try {
            Long voteId = pollService.getVoteIdByUserId(userId, newsfeedId);
            return ResponseEntity.ok(voteId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
