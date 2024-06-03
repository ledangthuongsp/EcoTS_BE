package com.example.EcoTS.Services.Achievement;

import com.example.EcoTS.Enum.AchievementType;
import com.example.EcoTS.Models.Results;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.ResultRepository;
import com.example.EcoTS.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {
    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void createResultsForAllUsers() {
        List<Users> users = userRepository.findAll();
        for (Users user : users) {
            Results results = new Results();
            results.setUsers(user);
            results.setNumberOfTimeDonate(0);
            results.setNumberOfTimeDetect(0);
            results.setMaximumPoints(0.0);
            results.setPointDonate(0.0);
            results.setSaveCo2(0.0);
            resultRepository.save(results);
        }
    }
}
