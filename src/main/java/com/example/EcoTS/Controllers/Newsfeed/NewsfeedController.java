package com.example.EcoTS.Controllers.Newsfeed;

import com.example.EcoTS.DTOs.Request.Newsfeed.NewsfeedRequest;
import com.example.EcoTS.DTOs.Request.Newsfeed.PollRequest;
import com.example.EcoTS.DTOs.Response.Newsfeed.NewsfeedResponse;
import com.example.EcoTS.Models.Newsfeed.Comment;
import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Models.Newsfeed.React;
import com.example.EcoTS.Repositories.Newsfeed.NewsfeedRepository;
import com.example.EcoTS.Services.Newsfeed.NewsfeedService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/newsfeed")
@Tag(name = "Newsfeed CRUD")
public class NewsfeedController {
    @Autowired
    private NewsfeedService newsfeedService;
    @Autowired
    private NewsfeedRepository newsfeedRepository;
    // CREATE: Add a new newsfeed
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Newsfeed> createNewsfeed(
            @RequestParam String content,
            @RequestParam (required = false) Long sponsorId,
            @RequestParam (required = false) Double pointForActivity,
            @RequestParam (required = false) Long userId,
            @RequestParam List<String> pollOptions, // Các lựa chọn cho Poll
            @RequestParam (required = true) Timestamp startedAt,
            @RequestParam (required = true) Timestamp endedAt,
            @RequestPart("files") List<MultipartFile> files // Danh sách file tải lên
    ) throws IOException {
        Newsfeed createdNewsfeed = newsfeedService.createNewsfeed(content, sponsorId, pointForActivity, userId, pollOptions, files, startedAt, endedAt);
        return ResponseEntity.ok(createdNewsfeed);
    }
    // READ: Get all newsfeeds
    @GetMapping("/getAll")
    public ResponseEntity<List<Newsfeed>> getAllNewsfeeds() {
        List<Newsfeed> newsfeeds = newsfeedService.getAllNewsfeed();
        return ResponseEntity.ok(newsfeeds);
    }
    @GetMapping(value = "/get-your-activity", name = "Cai nay la de lay nhung activity ma minh da post len")
    public ResponseEntity<List<Newsfeed>> getNewsfeedYourActivity(Long userId)
    {
        List<Newsfeed> newsfeeds = newsfeedService.getYourActivity(userId);
        return ResponseEntity.ok(newsfeeds);
    }
    // READ: Get a single newsfeed by I
    // DELETE: Delete a newsfeed by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteNewsfeed(@PathVariable Long id) {
        newsfeedService.deleteNewsfeed(id);
        return ResponseEntity.ok("Newsfeed deleted successfully");
    }
    @PostMapping("/{newsfeedId}/comments")
    public ResponseEntity<Newsfeed> addComment(
            @PathVariable Long newsfeedId,
            @RequestParam Long userId,
            @RequestParam String message,
            @RequestParam(required = false) List<String> imgUrls) {
        Newsfeed updatedNewsfeed = newsfeedService.addComment(newsfeedId, userId, message, imgUrls);
        return ResponseEntity.ok(updatedNewsfeed);
    }
    @PostMapping("/{newsfeedId}/reacts")
    public ResponseEntity<Newsfeed> addReact(
            @PathVariable Long newsfeedId,
            @RequestParam Long userId,
            @RequestParam boolean status) {
        Newsfeed updatedNewsfeed = newsfeedService.addReact(newsfeedId, userId, status);
        return ResponseEntity.ok(updatedNewsfeed);
    }
    @DeleteMapping("/{newsfeedId}/reacts/{reactId}")
    public ResponseEntity<Newsfeed> removeReact(
            @PathVariable Long newsfeedId,
            @PathVariable Long reactId) {
        Newsfeed updatedNewsfeed = newsfeedService.removeReact(newsfeedId, reactId);
        return ResponseEntity.ok(updatedNewsfeed);
    }

    // API cập nhật React thành false
    @PutMapping("/{newsfeedId}/reacts/update")
    public ResponseEntity<String> updateReactToFalse(
            @PathVariable Long newsfeedId,
            @RequestParam Long userId
    ) {
        newsfeedService.updateReactStatus(newsfeedId, userId);
        return ResponseEntity.ok("Status react update for userId: " + userId);
    }

    // Cập nhật comment
    @PutMapping("/{newsfeedId}/comments/{commentId}")
    public ResponseEntity<Newsfeed> updateComment(
            @PathVariable Long newsfeedId,
            @PathVariable Long commentId,
            @RequestParam String newMessage,
            @RequestParam(required = false) List<String> newImgUrls) {
        Newsfeed updatedNewsfeed = newsfeedService.updateComment(newsfeedId, commentId, newMessage, newImgUrls);
        return ResponseEntity.ok(updatedNewsfeed);
    }

    // Xóa comment
    @DeleteMapping("/{newsfeedId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long newsfeedId,
            @PathVariable Long commentId) {
        newsfeedService.deleteComment(newsfeedId, commentId);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    // Lấy toàn bộ comment của một newsfeed
    @GetMapping("/{newsfeedId}/comments")
    public ResponseEntity<List<Comment>> getAllComments(@PathVariable Long newsfeedId) {
        List<Comment> comments = newsfeedService.getAllComments(newsfeedId);
        return ResponseEntity.ok(comments);
    }

    // Lấy tổng số lượng react của một newsfeed
    @GetMapping("/{newsfeedId}/reacts/count")
    public ResponseEntity<Long> countReacts(@PathVariable Long newsfeedId) {
        long reactCount = newsfeedService.countReactsByNewsfeed(newsfeedId);
        return ResponseEntity.ok(reactCount);
    }

    // Lấy tổng số lượng comment của một newsfeed
    @GetMapping("/{newsfeedId}/comments/count")
    public ResponseEntity<Long> countComments(@PathVariable Long newsfeedId) {
        long commentCount = newsfeedService.countComments(newsfeedId);
        return ResponseEntity.ok(commentCount);
    }
    @GetMapping("/react-status/{newsfeedId}")
    public ResponseEntity<?> getReactStatus(
            @PathVariable Long newsfeedId,
            @RequestParam Long userId) {
        try {
            boolean status = newsfeedService.getReactStatus(newsfeedId, userId);
            return ResponseEntity.ok(Collections.singletonMap("status", status));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    @GetMapping("/get-newsfeed-by-sponsor-id")
    public ResponseEntity<List<Newsfeed>> getNewsfeedBySponsorId(@RequestParam Long sponsorId) {

        List<Newsfeed> newsfeedList = newsfeedRepository.findBySponsorId(sponsorId);
        return ResponseEntity.ok().body(newsfeedList);

    }
}
