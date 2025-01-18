package com.example.EcoTS.Services.Newsfeed;

import com.example.EcoTS.DTOs.Response.Newsfeed.PollOptionResponse;
import com.example.EcoTS.DTOs.Response.Newsfeed.PollResponse;
import com.example.EcoTS.DTOs.Response.Newsfeed.VoteResponse;
import com.example.EcoTS.Models.Newsfeed.Newsfeed;
import com.example.EcoTS.Models.Newsfeed.Poll;
import com.example.EcoTS.Models.Newsfeed.PollOption;
import com.example.EcoTS.Models.Newsfeed.Vote;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.Newsfeed.NewsfeedRepository;
import com.example.EcoTS.Repositories.Newsfeed.PollOptionRepository;
import com.example.EcoTS.Repositories.Newsfeed.PollRepository;
import com.example.EcoTS.Repositories.Newsfeed.VoteRepository;
import com.example.EcoTS.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PollService {
    @Autowired
    private PollOptionRepository pollOptionRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private NewsfeedRepository newsfeedRepository;
    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public PollOption addVote(Long newsfeedId, Long pollOptionId, Long userId, boolean status) {
        // Kiểm tra Newsfeed tồn tại
        Newsfeed newsfeed = newsfeedRepository.findById(newsfeedId)
                .orElseThrow(() -> new IllegalArgumentException("Newsfeed not found"));

        if (!newsfeed.getPollId().equals(pollOptionId)) {
            throw new IllegalArgumentException("PollOption does not belong to Newsfeed");
        }

        // Tạo vote mới
        Vote vote = Vote.builder()
                .userId(userId)
                .status(status)
                .build();

        // Lưu vote vào database
        Vote savedVote = voteRepository.save(vote);

        // Thêm vote ID vào PollOption
        PollOption pollOption = pollOptionRepository.findById(pollOptionId)
                .orElseThrow(() -> new IllegalArgumentException("PollOption not found"));
        pollOption.getVoteIds().add(savedVote.getId());

        // Lưu lại PollOption
        return pollOptionRepository.save(pollOption);
    }

    @Transactional
    public PollOption removeVote(Long newsfeedId, Long pollOptionId, Long voteId) {
        // Kiểm tra Newsfeed tồn tại
        Newsfeed newsfeed = newsfeedRepository.findById(newsfeedId)
                .orElseThrow(() -> new IllegalArgumentException("Newsfeed not found"));

        if (!newsfeed.getPollId().equals(pollOptionId)) {
            throw new IllegalArgumentException("PollOption does not belong to Newsfeed");
        }

        // Xóa vote khỏi database
        voteRepository.deleteById(voteId);

        // Loại bỏ vote ID khỏi danh sách voteIds trong PollOption
        PollOption pollOption = pollOptionRepository.findById(pollOptionId)
                .orElseThrow(() -> new IllegalArgumentException("PollOption not found"));
        pollOption.getVoteIds().remove(voteId);

        // Lưu lại PollOption
        return pollOptionRepository.save(pollOption);
    }
    @Transactional
    public PollResponse getPollByNewsfeedId(Long newsfeedId) {
        // Tìm Newsfeed
        Newsfeed newsfeed = newsfeedRepository.findById(newsfeedId)
                .orElseThrow(() -> new IllegalArgumentException("Newsfeed not found"));

        // Tìm Poll
        Poll poll = pollRepository.findById(newsfeed.getPollId())
                .orElseThrow(() -> new IllegalArgumentException("Poll not found"));

        // Fetch PollOption dựa trên pollOptionIds
        List<PollOption> pollOptions = poll.getPollOptionIds().stream()
                .map(pollOptionId -> pollOptionRepository.findById(pollOptionId)
                        .orElseThrow(() -> new IllegalArgumentException("PollOption not found")))
                .toList();

        // Xây dựng PollResponse
        return PollResponse.builder()
                .id(poll.getId())
                .title(poll.getTitle())
                .pollOptions(pollOptions.stream().map(option -> {
                    // Fetch Vote dựa trên voteIds
                    List<Vote> votes = option.getVoteIds().stream()
                            .map(voteId -> voteRepository.findById(voteId)
                                    .orElseThrow(() -> new IllegalArgumentException("Vote not found")))
                            .toList();

                    // Chuyển đổi Vote -> VoteResponse
                    List<VoteResponse> voteResponses = votes.stream().map(vote -> {
                        Users user = userRepository.findById(vote.getUserId())
                                .orElseThrow(() -> new IllegalArgumentException("User not found"));
                        return VoteResponse.builder()
                                .id(vote.getId())
                                .userId(user.getId())
                                .status(vote.isStatus())
                                .build();
                    }).toList();

                    // Chuyển đổi PollOption -> PollOptionResponse
                    return PollOptionResponse.builder()
                            .id(option.getId())
                            .type(option.getType())
                            .votes(voteResponses)
                            .build();
                }).toList())
                .build();
    }
}
