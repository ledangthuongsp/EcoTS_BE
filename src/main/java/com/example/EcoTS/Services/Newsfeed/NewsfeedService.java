package com.example.EcoTS.Services.Newsfeed;

import com.example.EcoTS.DTOs.Request.Newsfeed.NewsfeedRequest;
import com.example.EcoTS.DTOs.Request.Newsfeed.PollRequest;
import com.example.EcoTS.DTOs.Response.Newsfeed.NewsfeedResponse;
import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Models.Newsfeed.Poll;
import com.example.EcoTS.Models.Newsfeed.PollOption;
import com.example.EcoTS.Repositories.Newsfeed.NewsfeedRepository;
import com.example.EcoTS.Repositories.Newsfeed.PollOptionRepository;
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
    @Autowired
    private PollOptionRepository pollOptionRepository;

    // CREATE: Add a new newsfeed
    public Newsfeed createNewsfeed(String content, Long sponsorId, Double pointForActivity,
                                   Long userId, List<String> pollOptions,
                                   List<MultipartFile> files) throws IOException {
        // 1. Upload các file lên Cloudinary (hoặc dịch vụ khác) và lấy URL
        List<String> mediaUrls = cloudinaryService.uploadMultipleFilesNewsfeed(files);

        // 2. Tạo đối tượng Poll
        Poll poll = new Poll();
        poll.setTitle("Poll title"); // Thay bằng title mong muốn
        poll.setPollOptionIds(new ArrayList<>());
        Poll savedPoll = pollRepository.save(poll);

        // 3. Tạo các PollOption và liên kết với Poll
        for (String option : pollOptions) {
            PollOption pollOption = new PollOption();
            pollOption.setType(option);
            pollOption.setVoteIds(new ArrayList<>());
            PollOption savedOption = pollOptionRepository.save(pollOption);

            // Thêm ID của PollOption vào Poll
            savedPoll.getPollOptionIds().add(savedOption.getId());
        }

        // Cập nhật lại Poll sau khi thêm các Option
        pollRepository.save(savedPoll);

        // 4. Tạo đối tượng Newsfeed
        Newsfeed newsfeed = Newsfeed.builder()
                .content(content)
                .sponsorId(sponsorId)
                .pointForActivity(pointForActivity)
                .userId(userId)
                .mediaUrls(mediaUrls)
                .pollId(savedPoll.getId())
                .commentIds(new ArrayList<>())
                .reactIds(new ArrayList<>())
                .build();

        // 5. Lưu Newsfeed vào database
        return newsfeedRepository.save(newsfeed);
    }

    // READ: Get all newsfeeds
    public List<Newsfeed> getAllNewsfeed() {
        return newsfeedRepository.findAll();
    }


    public List<Newsfeed> getYourActivity (Long userId)
    {
        return newsfeedRepository.findByUserId(userId);
    }
    // DELETE: Delete a newsfeed by ID
    public void deleteNewsfeed(Long id) {
        if (newsfeedRepository.existsById(id)) {
            newsfeedRepository.deleteById(id);
        }
    }
}
