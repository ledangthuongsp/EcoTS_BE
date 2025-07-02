package com.example.EcoTS.Enum;

public enum RewardItemClaimStatus {
    PENDING,
    COLLECTED,// Đã yêu cầu, chờ nhận
    COMPLETED,  // Đã nhận
    CANCELLED   // Hủy (quá hạn hoặc nhân viên từ chối)
}
