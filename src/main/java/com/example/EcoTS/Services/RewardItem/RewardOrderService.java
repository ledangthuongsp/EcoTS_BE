package com.example.EcoTS.Services.RewardItem;

import com.example.EcoTS.DTOs.Response.RewardOrderResponse;
import com.example.EcoTS.Enum.RewardOrderStatus;
import com.example.EcoTS.Models.Locations;
import com.example.EcoTS.Models.Points;
import com.example.EcoTS.Models.Reward.RewardItem;
import com.example.EcoTS.Models.Reward.RewardItemLocation;
import com.example.EcoTS.Models.Reward.RewardOrder;
import com.example.EcoTS.Models.Users;
import com.example.EcoTS.Repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardOrderService {

    private final UserRepository userRepository;
    private final RewardItemRepository rewardItemRepository;
    private final LocationRepository locationRepository;
    private final RewardOrderRepository rewardOrderRepository;
    private final RewardItemLocationRepository rewardItemLocationRepository;
    private final PointRepository pointRepository;

    @Transactional
    public void redeemReward(Long userId, Long rewardItemId, Long locationId, int quantity) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Points points = pointRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Points not found with user"));
        RewardItem item = rewardItemRepository.findById(rewardItemId)
                .orElseThrow(() -> new RuntimeException("Reward item not found"));
        Locations location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        RewardItemLocation ril = rewardItemLocationRepository
                .findByRewardItemAndLocation(item, location)
                .orElseThrow(() -> new RuntimeException("Item not available at this location"));

        long totalCost = (long) (item.getPointCharge() * quantity);
        if (points.getPoint() < totalCost)
            throw new RuntimeException("Not enough points");
        if (ril.getStock() < quantity)
            throw new RuntimeException("Not enough stock");

        // Trừ điểm
        points.setPoint(points.getPoint() - totalCost);

        // Trừ stock, tăng pending
        ril.setStock(ril.getStock() - quantity);
        ril.setPending(ril.getPending() + quantity);
        // 3. Tạo đơn hàng
        RewardOrder order = RewardOrder.builder()
                .user(user)
                .rewardItem(item)
                .location(location)
                .quantity(quantity)
                .status(RewardOrderStatus.PENDING)
                .build();
        rewardOrderRepository.save(order);

        userRepository.save(user);

        rewardItemLocationRepository.save(ril);

        sendNotification(user.getUsername(), item.getItemName(), quantity);
    }

    public List<RewardOrderResponse> getUserRewardHistory(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<RewardOrder> orders = rewardOrderRepository.findByUserOrderByCreatedAtDesc(user);
        List<RewardOrderResponse> response = new ArrayList<>();

        for (RewardOrder order : orders) {
            RewardItem item = order.getRewardItem();
            String imageUrl = item.getRewardItemUrl() != null && !item.getRewardItemUrl().isEmpty()
                    ? item.getRewardItemUrl().get(0)
                    : null;

            response.add(RewardOrderResponse.builder()
                    .orderId(order.getId())
                    .rewardItemName(item.getItemName())
                    .rewardItemImage(imageUrl)
                    .quantity(order.getQuantity())
                    .locationName(order.getLocation().getLocationName())
                    .status(order.getStatus().toString())
                    .createdAt(order.getCreatedAt())
                    .build());
        }

        return response;
    }
    private void sendNotification(String username, String itemName, int quantity) {
        System.out.printf(" Notification: %s đã đổi %d x %s%n", username, quantity, itemName);
    }
    public List<RewardOrderResponse> getOrdersByLocationId(Long locationId) {
        Locations location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        List<RewardOrder> orders = rewardOrderRepository.findByLocationOrderByCreatedAtDesc(location);
        List<RewardOrderResponse> response = new ArrayList<>();

        for (RewardOrder order : orders) {
            RewardItem item = order.getRewardItem();
            String imageUrl = item.getRewardItemUrl() != null && !item.getRewardItemUrl().isEmpty()
                    ? item.getRewardItemUrl().get(0)
                    : null;

            response.add(RewardOrderResponse.builder()
                    .orderId(order.getId())
                    .rewardItemName(item.getItemName())
                    .rewardItemImage(imageUrl)
                    .quantity(order.getQuantity())
                    .locationName(location.getLocationName())
                    .status(order.getStatus().toString())
                    .createdAt(order.getCreatedAt())
                    .build());
        }

        return response;
    }
    @Transactional
    public void confirmOrder(Long orderId) {
        RewardOrder order = rewardOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != RewardOrderStatus.PENDING) {
            throw new RuntimeException("Order is not in PENDING status");
        }

        RewardItemLocation ril = rewardItemLocationRepository
                .findByRewardItemAndLocation(order.getRewardItem(), order.getLocation())
                .orElseThrow(() -> new RuntimeException("RewardItemLocation not found"));

        int quantity = order.getQuantity();
        Long currentPending = ril.getPending() != null ? ril.getPending() : 0L;

        if (currentPending < quantity) {
            throw new RuntimeException("Not enough pending stock to confirm order");
        }

        ril.setPending(currentPending - quantity);
        rewardItemLocationRepository.save(ril);

        order.setStatus(RewardOrderStatus.CONFIRMED);
        rewardOrderRepository.save(order);
    }
    @Transactional
    public void userConfirmOrder(Long orderId) {
        RewardOrder order = rewardOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != RewardOrderStatus.PENDING) {
            throw new RuntimeException("Only PENDING orders can be confirmed");
        }

        order.setStatus(RewardOrderStatus.CONFIRMED);
        rewardOrderRepository.save(order);
    }
    @Transactional
    public void cancelOrderByUser(Long orderId) {
        RewardOrder order = rewardOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != RewardOrderStatus.PENDING) {
            throw new RuntimeException("Order cannot be cancelled");
        }

        // Hoàn lại điểm
        Points points = pointRepository.findByUser(order.getUser())
                .orElseThrow(() -> new RuntimeException("Points not found"));

        long refund = (long) (order.getRewardItem().getPointCharge() * order.getQuantity());
        points.setPoint(points.getPoint() + refund);

        // Giảm pending
        RewardItemLocation ril = rewardItemLocationRepository
                .findByRewardItemAndLocation(order.getRewardItem(), order.getLocation())
                .orElseThrow(() -> new RuntimeException("RewardItemLocation not found"));

        ril.setPending(ril.getPending() - order.getQuantity());

        // Cập nhật trạng thái
        order.setStatus(RewardOrderStatus.CANCELLED_BY_USER);

        // Lưu
        pointRepository.save(points);
        rewardItemLocationRepository.save(ril);
        rewardOrderRepository.save(order);
    }
    @Transactional
    public void autoExpireOrders() {
        Timestamp threshold = Timestamp.valueOf(LocalDateTime.now().minusDays(7));
        List<RewardOrder> expiredOrders = rewardOrderRepository.findExpiredPendingOrders(threshold);

        for (RewardOrder order : expiredOrders) {
            Points points = pointRepository.findByUser(order.getUser())
                    .orElseThrow(() -> new RuntimeException("Points not found"));
            long refund = (long) (order.getRewardItem().getPointCharge() * order.getQuantity());
            points.setPoint(points.getPoint() + refund);

            RewardItemLocation ril = rewardItemLocationRepository
                    .findByRewardItemAndLocation(order.getRewardItem(), order.getLocation())
                    .orElseThrow(() -> new RuntimeException("RewardItemLocation not found"));
            ril.setPending(ril.getPending() - order.getQuantity());

            order.setStatus(RewardOrderStatus.EXPIRED);

            pointRepository.save(points);
            rewardItemLocationRepository.save(ril);
            rewardOrderRepository.save(order);
        }
    }

}
