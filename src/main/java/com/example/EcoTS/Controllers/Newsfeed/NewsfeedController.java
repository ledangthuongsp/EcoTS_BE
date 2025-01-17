package com.example.EcoTS.Controllers.Newsfeed;

import com.example.EcoTS.DTOs.Request.Newsfeed.NewsfeedRequest;
import com.example.EcoTS.DTOs.Request.Newsfeed.PollRequest;
import com.example.EcoTS.DTOs.Response.Newsfeed.NewsfeedResponse;
import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Models.Newsfeed.React;
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
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/newsfeed")
@Tag(name = "Newsfeed CRUD")
public class NewsfeedController {
    @Autowired
    private NewsfeedService newsfeedService;

    // CREATE: Add a new newsfeed
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Newsfeed> createNewsfeed(
            @RequestParam String content,
            @RequestParam (required = false) Long sponsorId,
            @RequestParam (required = false) Double pointForActivity,
            @RequestParam (required = false) Long userId,
            @RequestParam List<String> pollOptions, // Các lựa chọn cho Poll
            @RequestPart("files") List<MultipartFile> files // Danh sách file tải lên
    ) throws IOException {
        Newsfeed createdNewsfeed = newsfeedService.createNewsfeed(content, sponsorId, pointForActivity, userId, pollOptions, files);
        return ResponseEntity.ok(createdNewsfeed);
    }
    // READ: Get all newsfeeds
    @GetMapping("/getAll")
    public ResponseEntity<List<Newsfeed>> getAllNewsfeeds() {
        List<Newsfeed> newsfeeds = newsfeedService.getAllNewsfeed();
        return ResponseEntity.ok(newsfeeds);
    }
    @GetMapping("/get-your-activity")
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
    public ResponseEntity<List<Long>> getAllComments(@PathVariable Long newsfeedId) {
        List<Long> comments = newsfeedService.getAllComments(newsfeedId);
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
}
