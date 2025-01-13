package com.example.EcoTS.Services.Newsfeed;

import com.example.EcoTS.DTOs.Response.Newsfeed.PollOptionResponse;
import com.example.EcoTS.DTOs.Response.Newsfeed.PollResponse;
import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Models.Newsfeed.Poll;
import com.example.EcoTS.Models.Newsfeed.PollOption;
import com.example.EcoTS.Repositories.Newsfeed.NewsfeedRepository;
import com.example.EcoTS.Repositories.Newsfeed.PollOptionRepository;
import com.example.EcoTS.Repositories.Newsfeed.PollRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PollService {
    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private NewsfeedRepository newsfeedRepository;

    @Autowired
    private PollOptionRepository pollOptionRepository;
    @Autowired
    private ModelMapper modelMapper;

    // Create or update a poll for a newsfeed
    public PollResponse createPoll(Long newsfeedId, List<String> options) {
        Newsfeed newsfeed = newsfeedRepository.findById(newsfeedId)
                .orElseThrow(() -> new RuntimeException("Newsfeed not found"));

        Poll poll = new Poll();
        poll.setNewsfeed(newsfeed);

        List<PollOption> pollOptions = options.stream()
                .map(optionText -> {
                    PollOption option = new PollOption();
                    option.setOptionText(optionText);
                    option.setPoll(poll);
                    return option;
                })
                .collect(Collectors.toList());

        pollOptionRepository.saveAll(pollOptions);
        poll.setOptions(pollOptions);

        pollRepository.save(poll);

        return mapToResponseDTO(poll);
    }

    // Get poll for a specific newsfeed
    public PollResponse getPollForNewsfeed(Long newsfeedId) {
        Poll poll = pollRepository.findByNewsfeedId(newsfeedId)
                .orElseThrow(() -> new RuntimeException("Poll not found"));

        return mapToResponseDTO(poll);
    }

    // Helper method to map Poll entity to Response DTO
    private PollResponse mapToResponseDTO(Poll poll) {
        return modelMapper.map(poll, PollResponse.class);
    }
}
