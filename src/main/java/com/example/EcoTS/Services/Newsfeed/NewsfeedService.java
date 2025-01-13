package com.example.EcoTS.Services.Newsfeed;

import com.example.EcoTS.DTOs.Request.Newsfeed.NewsfeedRequest;
import com.example.EcoTS.DTOs.Response.Newsfeed.NewsfeedResponse;
import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Repositories.Newsfeed.CommentRepository;
import com.example.EcoTS.Repositories.Newsfeed.NewsfeedRepository;
import com.example.EcoTS.Repositories.Newsfeed.PollRepository;
import com.example.EcoTS.Repositories.Newsfeed.ReactRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsfeedService {

    @Autowired
    private NewsfeedRepository newsfeedRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReactRepository reactRepository;

    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private ModelMapper modelMapper;
    // Create or Update Newsfeed
    public NewsfeedResponse createOrUpdateNewsfeed(NewsfeedRequest requestDTO) {
        Newsfeed newsfeed = new Newsfeed();
        newsfeed.setContent(requestDTO.getContent());
        newsfeed.setMediaUrls(requestDTO.getMediaUrls());
        newsfeed.setSponsorId(requestDTO.getSponsorId());
        newsfeed.setPointForActivity(requestDTO.getPointForActivity());
        newsfeed.setCreatedBy(requestDTO.getCreatedBy());
        newsfeed.setCreatedById(requestDTO.getCreatedById());

        newsfeedRepository.save(newsfeed);

        // Map Entity to Response DTO
        return mapToResponseDTO(newsfeed);
    }
    // Helper method to map Newsfeed entity to Response DTO
    public NewsfeedResponse mapToResponseDTO(Newsfeed newsfeed) {
        return modelMapper.map(newsfeed, NewsfeedResponse.class);
    }
    // Get a Newsfeed by ID
    public NewsfeedResponse getNewsfeed(Long id) {
        Newsfeed newsfeed = newsfeedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Newsfeed not found"));
        return mapToResponseDTO(newsfeed);
    }

    // Get all Newsfeeds
    public List<NewsfeedResponse> getAllNewsfeeds() {
        List<Newsfeed> newsfeeds = newsfeedRepository.findAll();
        return newsfeeds.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
}
