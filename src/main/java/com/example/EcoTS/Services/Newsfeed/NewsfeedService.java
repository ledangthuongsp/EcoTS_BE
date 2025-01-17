package com.example.EcoTS.Services.Newsfeed;

import com.example.EcoTS.DTOs.Request.Newsfeed.NewsfeedRequest;
import com.example.EcoTS.DTOs.Request.Newsfeed.PollRequest;
import com.example.EcoTS.DTOs.Response.Newsfeed.NewsfeedResponse;
import com.example.EcoTS.Models.Newsfeed.*;
import com.example.EcoTS.Repositories.Newsfeed.*;
import com.example.EcoTS.Services.CloudinaryService.CloudinaryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReactRepository reactRepository;

    @Autowired
    private VoteRepository voteRepository;
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
    @Transactional
    public Newsfeed addComment(Long newsfeedId, Long userId, String message, List<String> imgUrls) {
        // Tạo comment mới
        Comment comment = Comment.builder()
                .userId(userId)
                .message(message)
                .imgUrls(imgUrls)
                .build();

        // Lưu comment vào database
        Comment savedComment = commentRepository.save(comment);

        // Thêm comment ID vào Newsfeed
        Newsfeed newsfeed = newsfeedRepository.findById(newsfeedId)
                .orElseThrow(() -> new IllegalArgumentException("Newsfeed not found"));
        newsfeed.getCommentIds().add(savedComment.getId());
        // Lưu lại Newsfeed
        return newsfeedRepository.save(newsfeed);
    }
    @Transactional
    public Newsfeed addReact(Long newsfeedId, Long userId, boolean status) {
        // Tạo react mới
        React react = React.builder()
                .userId(userId)
                .status(status)
                .build();

        // Lưu react vào database
        React savedReact = reactRepository.save(react);

        // Thêm react ID vào Newsfeed
        Newsfeed newsfeed = newsfeedRepository.findById(newsfeedId)
                .orElseThrow(() -> new IllegalArgumentException("Newsfeed not found"));
        newsfeed.getReactIds().add(savedReact.getId());

        // Lưu lại Newsfeed
        return newsfeedRepository.save(newsfeed);
    }
    @Transactional
    public Newsfeed removeReact(Long newsfeedId, Long reactId) {
        // Xóa react khỏi database
        reactRepository.deleteById(reactId);

        // Loại bỏ react ID khỏi danh sách reactIds trong Newsfeed
        Newsfeed newsfeed = newsfeedRepository.findById(newsfeedId)
                .orElseThrow(() -> new IllegalArgumentException("Newsfeed not found"));
        newsfeed.getReactIds().remove(reactId);

        // Lưu lại Newsfeed
        return newsfeedRepository.save(newsfeed);
    }
    @Transactional
    // Cập nhật React thành false dựa trên newsfeedId và userId
    public React updateReactStatus(Long newsfeedId, Long userId) {
        return newsfeedRepository.findById(newsfeedId)
                .map(newsfeed -> newsfeed.getReactIds().stream()
                        .map(reactId -> reactRepository.findById(reactId))
                        .filter(Optional::isPresent) // Kiểm tra React tồn tại
                        .map(Optional::get)          // Lấy React
                        .filter(react -> react.getUserId().equals(userId)) // Đúng userId
                        .findFirst()                 // Lấy React đầu tiên
                        .map(react -> {
                            react.setStatus(!react.isStatus()); // Đảo trạng thái
                            return reactRepository.save(react); // Cập nhật trong DB
                        })
                        .orElseThrow(() -> new RuntimeException("React not found for userId: " + userId)))
                .orElseThrow(() -> new RuntimeException("Newsfeed not found with id: " + newsfeedId));
    }

    // Cập nhật comment
    @Transactional
    public Newsfeed updateComment(Long newsfeedId, Long commentId, String newMessage, List<String> newImgUrls) {
        Optional<Newsfeed> optionalNewsfeed = newsfeedRepository.findById(newsfeedId);
        if (optionalNewsfeed.isPresent()) {
            Newsfeed newsfeed = optionalNewsfeed.get();

            // Logic để cập nhật comment, tìm comment theo commentId và thay đổi nội dung
            // Bạn có thể mở rộng logic này để tìm và cập nhật comment theo commentId
            // Chẳng hạn nếu có model riêng cho Comment, có thể xử lý như sau
            int commentIndex = -1;
            for (int i = 0; i < newsfeed.getCommentIds().size(); i++) {
                if (newsfeed.getCommentIds().get(i).equals(commentId)) {
                    commentIndex = i;
                    break;
                }
            }

            if (commentIndex != -1) {
                // Cập nhật comment
                // Cập nhật nội dung comment tại index tương ứng
                // Lưu ý: logic cập nhật này có thể cần thêm vào việc lưu dữ liệu comment thực tế
            } else {
                throw new RuntimeException("Comment not found");
            }

            newsfeedRepository.save(newsfeed);
            return newsfeed;
        } else {
            throw new RuntimeException("Newsfeed not found");
        }
    }
    @Transactional
    // Xóa comment
    public Newsfeed deleteComment(Long newsfeedId, Long commentId) {
        Optional<Newsfeed> optionalNewsfeed = newsfeedRepository.findById(newsfeedId);
        if (optionalNewsfeed.isPresent()) {
            Newsfeed newsfeed = optionalNewsfeed.get();

            // Tìm và xóa comment theo commentId
            int commentIndex = -1;
            for (int i = 0; i < newsfeed.getCommentIds().size(); i++) {
                if (newsfeed.getCommentIds().get(i).equals(commentId)) {
                    commentIndex = i;
                    break;
                }
            }

            if (commentIndex != -1) {
                // Xóa comment
                newsfeed.getCommentIds().remove(commentIndex);
                newsfeedRepository.save(newsfeed);
                return newsfeed;
            } else {
                throw new RuntimeException("Comment not found");
            }
        } else {
            throw new RuntimeException("Newsfeed not found");
        }
    }
    @Transactional
    // Lấy toàn bộ comment của một newsfeed
    public List<Long> getAllComments(Long newsfeedId) {
        Optional<Newsfeed> optionalNewsfeed = newsfeedRepository.findById(newsfeedId);
        if (optionalNewsfeed.isPresent()) {
            return optionalNewsfeed.get().getCommentIds();  // Trả về danh sách commentIds
        } else {
            throw new RuntimeException("Newsfeed not found");
        }
    }
    @Transactional
    // Lấy toàn bộ React có giá trị true (status = true)
    public List<React> getAllReactsTrue(Long newsfeedId) {
        Optional<Newsfeed> newsfeed = newsfeedRepository.findById(newsfeedId);
        if (newsfeed.isPresent()) {
            List<Long> reactIds = newsfeed.get().getReactIds();
            return reactRepository.findAllById(reactIds).stream()
                    .filter(React::isStatus) // Lọc những react có status = true
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Newsfeed not found");
        }
    }
    @Transactional
    // Lấy toàn bộ React và trả về số lượng
    public long countReacts(Long newsfeedId) {
        Optional<Newsfeed> newsfeed = newsfeedRepository.findById(newsfeedId);
        return newsfeed.map(nf -> reactRepository.findAllById(nf.getReactIds()).size())
                .orElseThrow(() -> new RuntimeException("Newsfeed not found"));
    }
    @Transactional
    // Lấy tất cả comment và trả về số lượng
    public long countComments(Long newsfeedId) {
        Optional<Newsfeed> optionalNewsfeed = newsfeedRepository.findById(newsfeedId);
        if (optionalNewsfeed.isPresent()) {
            return optionalNewsfeed.get().getCommentIds().size();
        } else {
            throw new RuntimeException("Newsfeed not found");
        }
    }
}
