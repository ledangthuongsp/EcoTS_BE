package com.example.EcoTS.Services.DonationService;

import com.example.EcoTS.Models.DonationHistory;
import com.example.EcoTS.Repositories.DonationHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonationHistoryService {
    @Autowired
    private DonationHistoryRepository donationHistoryRepository;

    public List<DonationHistory> getDonationHistoryByUserId(Long userId) {
        return donationHistoryRepository.findByUserId(userId);
    }
}