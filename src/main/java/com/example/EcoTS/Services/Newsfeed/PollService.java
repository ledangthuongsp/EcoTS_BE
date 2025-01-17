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

}
