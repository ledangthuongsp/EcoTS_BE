package com.example.EcoTS.Services.Newsfeed;

import com.example.EcoTS.DTOs.Request.Newsfeed.PollRequest;
import com.example.EcoTS.DTOs.Response.Newsfeed.NewsfeedResponse;
import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Models.Newsfeed.Poll;
import com.example.EcoTS.Models.Newsfeed.PollOption;
import com.example.EcoTS.Repositories.Newsfeed.NewsfeedRepository;
import com.example.EcoTS.Repositories.Newsfeed.PollRepository;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsfeedService {

    @Autowired
    private NewsfeedRepository newsfeedRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private PollRepository pollRepository;
    // CREATE: Add a new newsfeed
    public Newsfeed createNewsfeed(String title, String content, List<MultipartFile> mediaUrls, PollRequest pollRequest) throws IOException {
        Newsfeed newsfeed = new Newsfeed();
        newsfeed.setContent(title);
        newsfeed.setContent(content);

        // Save media files to storage and update the mediaUrls
        List<String> mediaPaths = cloudinaryService.uploadMultipleFilesNewsfeed(mediaUrls);
        newsfeed.setMediaUrls(mediaPaths);

        // Create Poll if provided
        if (pollRequest != null) {
            Poll poll = new Poll();
            poll.setNewsfeed(newsfeed);  // Associate Poll with the Newsfeed

            // Create Poll Options from the request
            Poll finalPoll = poll;
            List<PollOption> pollOptions = pollRequest.getOptions().stream()
                    .map(optionText -> new PollOption(null, finalPoll, optionText, new ArrayList<>()))
                    .collect(Collectors.toList());

            poll.setOptions(pollOptions);

            // Save Poll and associate it with the Newsfeed
            poll = pollRepository.save(poll);  // No need for final keyword here
            newsfeed.setPoll(poll);
        }

        // Set default Comments and Reactions as empty or null
        newsfeed.setComments(Collections.emptyList());
        newsfeed.setReactions(Collections.emptyList());

        return newsfeedRepository.save(newsfeed);
    }



    // READ: Get all newsfeeds
    public List<NewsfeedResponse> getAllNewsfeeds() {
        List<Newsfeed> newsfeedList = newsfeedRepository.findAll();
        return Collections.singletonList(modelMapper.map(newsfeedList, NewsfeedResponse.class));
    }

    // READ: Get a single newsfeed by ID
    public Newsfeed getNewsfeedById(Long id) {
        return newsfeedRepository.findById(id).orElse(null);
    }

    // UPDATE: Update an existing newsfeed by ID
    public Newsfeed updateNewsfeed(Long id, String title, String content, List<MultipartFile> mediaUrls) throws IOException {
        Newsfeed newsfeed = newsfeedRepository.findById(id).orElse(null);
        if (newsfeed == null) {
            return null; // If not found, return null
        }

        // Update fields
        if (title != null && !title.isEmpty()) {
            newsfeed.setContent(title);
        }
        if (content != null && !content.isEmpty()) {
            newsfeed.setContent(content);
        }

        // Handle media files update
        if (mediaUrls != null && !mediaUrls.isEmpty()) {
            List<String> mediaPaths = cloudinaryService.uploadMultipleFilesNewsfeed(mediaUrls);
            newsfeed.setMediaUrls(mediaPaths);
        }

        return newsfeedRepository.save(newsfeed);
    }

    // DELETE: Delete a newsfeed by ID
    public boolean deleteNewsfeed(Long id) {
        if (newsfeedRepository.existsById(id)) {
            newsfeedRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
