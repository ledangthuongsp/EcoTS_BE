package com.example.EcoTS.Services.Newsfeed;

import com.example.EcoTS.Models.Newsfeed.PollOption;
import com.example.EcoTS.Models.Newsfeed.Vote;
import com.example.EcoTS.Repositories.Newsfeed.PollOptionRepository;
import com.example.EcoTS.Repositories.Newsfeed.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PollService {

    @Autowired
    private PollOptionRepository pollOptionRepository;
    @Autowired
    private VoteRepository voteRepository;

    @Transactional
    public PollOption addVote(Long pollOptionId, Long userId, boolean status) {
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
    public PollOption removeVote(Long pollOptionId, Long voteId) {
        // Xóa vote khỏi database
        voteRepository.deleteById(voteId);

        // Loại bỏ vote ID khỏi danh sách voteIds trong PollOption
        PollOption pollOption = pollOptionRepository.findById(pollOptionId)
                .orElseThrow(() -> new IllegalArgumentException("PollOption not found"));
        pollOption.getVoteIds().remove(voteId);

        // Lưu lại PollOption
        return pollOptionRepository.save(pollOption);
    }
}
