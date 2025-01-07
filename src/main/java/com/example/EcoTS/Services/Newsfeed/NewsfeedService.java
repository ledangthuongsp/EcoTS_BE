package com.example.EcoTS.Services.Newsfeed;

import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Repositories.Newsfeed.NewsfeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsfeedService {
    @Autowired
    private NewsfeedRepository newsfeedRepository;

    // Create a new newsfeed post
    public Newsfeed createNewsfeed(Newsfeed newsfeed) {
        return newsfeedRepository.save(newsfeed);
    }

    // Get all newsfeed posts
    public List<Newsfeed> getAllNewsfeeds() {
        return newsfeedRepository.findAll();
    }

    // Get a specific newsfeed post by ID
    public Optional<Newsfeed> getNewsfeedById(Long id) {
        return newsfeedRepository.findById(id);
    }

    // Update a newsfeed post
    public Newsfeed updateNewsfeed(Long id, Newsfeed updatedNewsfeed) {
        return newsfeedRepository.findById(id).map(newsfeed -> {
            newsfeed.setContent(updatedNewsfeed.getContent());
            newsfeed.setMediaUrls(updatedNewsfeed.getMediaUrls());
            newsfeed.setSponsorId(updatedNewsfeed.getSponsorId());
            newsfeed.setPointForActivity(updatedNewsfeed.getPointForActivity());
            return newsfeedRepository.save(newsfeed);
        }).orElseThrow(() -> new RuntimeException("Newsfeed not found"));
    }

    // Delete a newsfeed post
    public void deleteNewsfeed(Long id) {
        newsfeedRepository.deleteById(id);
    }
}
