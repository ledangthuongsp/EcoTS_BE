package com.example.EcoTS.Controllers.Newsfeed;

import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Services.Newsfeed.NewsfeedService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/newsfeed")
@Tag(name = "Newsfeed CRUD")
public class NewsfeedController {
    @Autowired
    private NewsfeedService newsfeedService;

    // Create a new newsfeed post
    @PostMapping
    public ResponseEntity<Newsfeed> createNewsfeed(@RequestBody Newsfeed newsfeed) {
        Newsfeed createdNewsfeed = newsfeedService.createNewsfeed(newsfeed);
        return new ResponseEntity<>(createdNewsfeed, HttpStatus.CREATED);
    }

    // Get all newsfeed posts
    @GetMapping
    public ResponseEntity<List<Newsfeed>> getAllNewsfeeds() {
        List<Newsfeed> newsfeeds = newsfeedService.getAllNewsfeeds();
        return new ResponseEntity<>(newsfeeds, HttpStatus.OK);
    }

    // Get a specific newsfeed post by ID
    @GetMapping("/{id}")
    public ResponseEntity<Newsfeed> getNewsfeedById(@PathVariable Long id) {
        Optional<Newsfeed> newsfeed = newsfeedService.getNewsfeedById(id);
        return newsfeed.map(ResponseEntity::ok).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a newsfeed post
    @PutMapping("/{id}")
    public ResponseEntity<Newsfeed> updateNewsfeed(@PathVariable Long id, @RequestBody Newsfeed newsfeed) {
        try {
            Newsfeed updatedNewsfeed = newsfeedService.updateNewsfeed(id, newsfeed);
            return new ResponseEntity<>(updatedNewsfeed, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a newsfeed post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNewsfeed(@PathVariable Long id) {
        newsfeedService.deleteNewsfeed(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
