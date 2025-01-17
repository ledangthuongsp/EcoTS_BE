package com.example.EcoTS.Controllers.Newsfeed;

import com.example.EcoTS.Models.Newsfeed.PollOption;
import com.example.EcoTS.Services.Newsfeed.PollService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/poll")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Poll")
public class PollController {
    @Autowired
    private PollService pollService;

    @PostMapping("/{pollOptionId}/votes")
    public ResponseEntity<PollOption> addVote(
            @PathVariable Long pollOptionId,
            @RequestParam Long userId,
            @RequestParam boolean status) {
        PollOption updatedPollOption = pollService.addVote(pollOptionId, userId, status);
        return ResponseEntity.ok(updatedPollOption);
    }
    @DeleteMapping("/{pollOptionId}/votes/{voteId}")
    public ResponseEntity<PollOption> removeVote(
            @PathVariable Long pollOptionId,
            @PathVariable Long voteId) {
        PollOption updatedPollOption = pollService.removeVote(pollOptionId, voteId);
        return ResponseEntity.ok(updatedPollOption);
    }
}
