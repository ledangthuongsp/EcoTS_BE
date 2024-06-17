package com.example.EcoTS.Services.Achievement;

import com.example.EcoTS.DTOs.Request.Achievement.AchievementAllProgressDTO;
import com.example.EcoTS.DTOs.Request.Achievement.AchievementProgressDTO;
import com.example.EcoTS.Enum.AchievementType;
import com.example.EcoTS.Models.AchievementLevel;
import com.example.EcoTS.Models.Results;
import com.example.EcoTS.Models.UserAchievement;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.*;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ResultService {
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AchievementLevelRepository achievementLevelRepository;
    @Autowired
    private AchievementRepository achievementRepository;
    @Autowired
    private UserAchievementRepository userAchievementRepository;
    @Autowired
    private UserAchievementService userAchievementService;
    @Transactional
    public void createResultsForAllUsers() {
        List<Users> users = userRepository.findAll();
        for (Users user : users) {
            Results results = new Results();
            results.setUser(user);
            results.setNumberOfTimeDonate(0);
            results.setNumberOfTimeDetect(0);
            results.setMaximumPoints(0.0);
            results.setPointDonate(0.0);
            results.setSaveCo2(0.0);
            resultRepository.save(results);
        }
    }
    @Transactional
    public AchievementProgressDTO getAchievementProgress(Long userId, Long achievementLevelId) {
        Users users = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Results results = resultRepository.findByUser(users)
                .orElseThrow(() -> new RuntimeException("Results not found"));
        AchievementLevel achievementLevel = achievementLevelRepository.findById(achievementLevelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid AchievementLevel ID"));

        double progress = 0.0;
        AchievementType achievementType = achievementLevel.getAchievement().getType();
        Long maxIndex = achievementLevel.getMaxIndex();

        switch (achievementType) {
            case COUNT_DONATE:
                progress = (double) results.getNumberOfTimeDonate() / maxIndex;
                break;
            case TOTAL_POINTS_DONATE:
                progress = results.getPointDonate() / maxIndex;
                break;
            case USER_MAX_POINT:
                progress = results.getMaximumPoints() / maxIndex;
                break;
            case SAVE_CO2:
                progress = results.getSaveCo2() / maxIndex;
                break;
            case USE_CAMERA_DETECT:
                progress = (double) results.getNumberOfTimeDetect() / maxIndex;
                break;
            default:
                throw new IllegalArgumentException("Invalid AchievementType: " + achievementType);
        }

        return new AchievementProgressDTO(achievementType, progress);
    }
    @Transactional
    public List<AchievementAllProgressDTO> getAllAchievementProgress(Long userId) {
        Users users = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Results results = resultRepository.findByUser(users)
                .orElseThrow(() -> new RuntimeException("Results not found"));
        List<AchievementLevel> achievementLevels = achievementLevelRepository.findAll();

        List<AchievementAllProgressDTO> progressList = new ArrayList<>();

        for (AchievementLevel achievementLevel : achievementLevels) {
            double progress = 0.0;
            double currentIndex = 0.0;
            AchievementType achievementType = achievementLevel.getAchievement().getType();
            String imgUrl = achievementLevel.getImgUrl();
            String iconUrl = achievementLevel.getIconUrl();
            Long maxIndex = achievementLevel.getMaxIndex();
            switch (achievementType) {
                case COUNT_DONATE:
                    progress = (double) results.getNumberOfTimeDonate() / maxIndex;
                    if(progress >= 1)
                    {
                        Optional<UserAchievement> userAchievement = userAchievementRepository.findByUser(users);
                        if(userAchievement.isPresent())
                        {
                            UserAchievement currentUserAchievement = userAchievement.get();
                            List<String> badgeUrl = currentUserAchievement.getBadgeUrl();
                            // Kiểm tra xem URL của huy hiệu đã tồn tại trong danh sách huy hiệu của người dùng chưa
                            if(!badgeUrl.contains(iconUrl)) {
                                badgeUrl.add(iconUrl);
                                currentUserAchievement.setBadgeUrl(badgeUrl);
                                userAchievementRepository.save(currentUserAchievement);
                            }
                        }
                        else
                        {
                            List<String> badgeUrl = new ArrayList<>();
                            badgeUrl.add(iconUrl);
                            UserAchievement newUserAchievement = userAchievementService.createNew(userId, badgeUrl);
                            userAchievementRepository.save(newUserAchievement); // Lưu mới UserAchievement
                        }
                    }
                    currentIndex = (double) results.getNumberOfTimeDonate();
                    break;
                case TOTAL_POINTS_DONATE:
                    progress = results.getPointDonate() / maxIndex;
                    if(progress >= 1)
                    {
                        Optional<UserAchievement> userAchievement = userAchievementRepository.findByUser(users);
                        if(userAchievement.isPresent())
                        {
                            UserAchievement currentUserAchievement = userAchievement.get();
                            List<String> badgeUrl = currentUserAchievement.getBadgeUrl();
                            // Kiểm tra xem URL của huy hiệu đã tồn tại trong danh sách huy hiệu của người dùng chưa
                            if(!badgeUrl.contains(iconUrl)) {
                                badgeUrl.add(iconUrl);
                                currentUserAchievement.setBadgeUrl(badgeUrl);
                                userAchievementRepository.save(currentUserAchievement);
                            }
                        }
                        else
                        {
                            List<String> badgeUrl = new ArrayList<>();
                            badgeUrl.add(iconUrl);
                            UserAchievement newUserAchievement = userAchievementService.createNew(userId, badgeUrl);
                            userAchievementRepository.save(newUserAchievement); // Lưu mới UserAchievement
                        }
                    }
                    currentIndex = results.getPointDonate();
                    break;
                case USER_MAX_POINT:
                    progress = results.getMaximumPoints() / maxIndex;
                    if(progress >= 1)
                    {
                        Optional<UserAchievement> userAchievement = userAchievementRepository.findByUser(users);
                        if(userAchievement.isPresent())
                        {
                            UserAchievement currentUserAchievement = userAchievement.get();
                            List<String> badgeUrl = currentUserAchievement.getBadgeUrl();
                            // Kiểm tra xem URL của huy hiệu đã tồn tại trong danh sách huy hiệu của người dùng chưa
                            if(!badgeUrl.contains(iconUrl)) {
                                badgeUrl.add(iconUrl);
                                currentUserAchievement.setBadgeUrl(badgeUrl);
                                userAchievementRepository.save(currentUserAchievement);
                            }
                        }
                        else
                        {
                            List<String> badgeUrl = new ArrayList<>();
                            badgeUrl.add(iconUrl);
                            UserAchievement newUserAchievement = userAchievementService.createNew(userId, badgeUrl);
                            userAchievementRepository.save(newUserAchievement); // Lưu mới UserAchievement
                        }
                    }
                    currentIndex = results.getMaximumPoints();
                    break;
                case SAVE_CO2:
                    progress = results.getSaveCo2() / maxIndex;
                    if(progress >= 1)
                    {
                        Optional<UserAchievement> userAchievement = userAchievementRepository.findByUser(users);
                        if(userAchievement.isPresent())
                        {
                            UserAchievement currentUserAchievement = userAchievement.get();
                            List<String> badgeUrl = currentUserAchievement.getBadgeUrl();
                            // Kiểm tra xem URL của huy hiệu đã tồn tại trong danh sách huy hiệu của người dùng chưa
                            if(!badgeUrl.contains(iconUrl)) {
                                badgeUrl.add(iconUrl);
                                currentUserAchievement.setBadgeUrl(badgeUrl);
                                userAchievementRepository.save(currentUserAchievement);
                            }
                        }
                        else
                        {
                            List<String> badgeUrl = new ArrayList<>();
                            badgeUrl.add(iconUrl);
                            UserAchievement newUserAchievement = userAchievementService.createNew(userId, badgeUrl);
                            userAchievementRepository.save(newUserAchievement); // Lưu mới UserAchievement
                        }
                    }
                    currentIndex = results.getSaveCo2();
                    break;
                case USE_CAMERA_DETECT:
                    progress = (double) results.getNumberOfTimeDetect() / maxIndex;
                    if(progress >= 1)
                    {
                        Optional<UserAchievement> userAchievement = userAchievementRepository.findByUser(users);
                        if(userAchievement.isPresent())
                        {
                            UserAchievement currentUserAchievement = userAchievement.get();
                            List<String> badgeUrl = currentUserAchievement.getBadgeUrl();
                            // Kiểm tra xem URL của huy hiệu đã tồn tại trong danh sách huy hiệu của người dùng chưa
                            if(!badgeUrl.contains(iconUrl)) {
                                badgeUrl.add(iconUrl);
                                currentUserAchievement.setBadgeUrl(badgeUrl);
                                userAchievementRepository.save(currentUserAchievement);
                            }
                        }
                        else
                        {
                            List<String> badgeUrl = new ArrayList<>();
                            badgeUrl.add(iconUrl);
                            UserAchievement newUserAchievement = userAchievementService.createNew(userId, badgeUrl);
                            userAchievementRepository.save(newUserAchievement); // Lưu mới UserAchievement
                        }
                    }
                    currentIndex = (double) results.getNumberOfTimeDetect();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid AchievementType: " + achievementType);
            }
            progressList.add(new AchievementAllProgressDTO(achievementLevel.getName(),achievementLevel.getId(), achievementLevel.getAchievement().getId()
                    , currentIndex, maxIndex, progress, imgUrl,iconUrl));
        }
        return progressList;
    }
}
