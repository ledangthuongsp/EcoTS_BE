package com.example.EcoTS.Services.RewardItem;

import com.example.EcoTS.DTOs.Response.RewardNotificationDTO;
import com.example.EcoTS.DTOs.Response.RewardOrderResponse;
import com.example.EcoTS.Enum.RewardOrderStatus;
import com.example.EcoTS.Models.*;
import com.example.EcoTS.Models.Reward.*;
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
    private final RewardNotificationRepository rewardNotificationRepository;

    @Transactional
    public void redeemReward(Long userId, Long rewardItemId, Long locationId, int quantity) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Points points = pointRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Points not found with user"));
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

        points.setPoint(points.getPoint() - totalCost);
        ril.setStock(ril.getStock() - quantity);
        ril.setPending(ril.getPending() + quantity);

        RewardOrder order = RewardOrder.builder()
                .user(user)
                .rewardItem(item)
                .location(location)
                .quantity(quantity)
                .status(RewardOrderStatus.PENDING)
                .build();
        rewardOrderRepository.save(order);

        // Create notification for reward redemption
        RewardNotification notification = RewardNotification.builder()
                .userId(user.getId())
                .rewardItem(item)
                .quantity(quantity)
                .location(location)
                .status(RewardOrderStatus.PENDING)
                .title("Đơn hàng của bạn đang được chuẩn bị")
                .description("Đơn hàng của bạn với số lượng " + quantity + " x " + item.getItemName() + " được chuẩn bị tại địa điểm " + location.getLocationName() + ".")
                .isRead(false)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        rewardNotificationRepository.save(notification);
        userRepository.save(user);
        rewardItemLocationRepository.save(ril);
    }

    @Transactional
    public void employeeConfirmOrder(Long orderId) {
        RewardOrder order = rewardOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (order.getStatus() != RewardOrderStatus.PENDING)
            throw new RuntimeException("Only PENDING orders can be confirmed by employee");

        order.setStatus(RewardOrderStatus.WAITING_FOR_USER);
        rewardOrderRepository.save(order);

        // Create notification for employee confirmation
        RewardNotification notification = RewardNotification.builder()
                .userId(order.getUser().getId())
                .rewardItem(order.getRewardItem())
                .quantity(order.getQuantity())
                .location(order.getLocation())
                .status(RewardOrderStatus.WAITING_FOR_USER)
                .title("Đơn hàng của bạn đã được chuẩn bị thành công !")
                .description("Đơn hàng của bạn với số lượng " + order.getQuantity() + " x " + order.getRewardItem().getItemName() + " đang chờ bạn đến lấy.")
                .isRead(false)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        rewardNotificationRepository.save(notification);
    }

    @Transactional
    public void userConfirmOrder(Long orderId) {
        RewardOrder order = rewardOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (order.getStatus() != RewardOrderStatus.WAITING_FOR_USER)
            throw new RuntimeException("Only WAITING_FOR_USER orders can be confirmed by user");

        RewardItemLocation ril = rewardItemLocationRepository
                .findByRewardItemAndLocation(order.getRewardItem(), order.getLocation())
                .orElseThrow(() -> new RuntimeException("RewardItemLocation not found"));

        if (ril.getPending() < order.getQuantity())
            throw new RuntimeException("Not enough pending stock to confirm order");

        ril.setPending(ril.getPending() - order.getQuantity());
        order.setStatus(RewardOrderStatus.CONFIRMED);

        rewardItemLocationRepository.save(ril);
        rewardOrderRepository.save(order);

        // Create notification for user confirmation
        RewardNotification notification = RewardNotification.builder()
                .userId(order.getUser().getId())
                .rewardItem(order.getRewardItem())
                .quantity(order.getQuantity())
                .location(order.getLocation())
                .status(RewardOrderStatus.CONFIRMED)
                .title("Đã nhận hàng thành công")
                .description("Đơn hàng với số lượng " + order.getQuantity() + " x " + order.getRewardItem().getItemName() + " đã được người dùng nhận hàng thành công.")
                .isRead(false)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .updatedAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        rewardNotificationRepository.save(notification);
    }

    public List<RewardNotificationDTO> getUserNotifications(Long userId) {
        List<RewardNotification> notifications = rewardNotificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<RewardNotificationDTO> response = new ArrayList<>();

        for (RewardNotification notification : notifications) {
            RewardNotificationDTO dto = RewardNotificationDTO.builder()
                    .id(notification.getId())
                    .title(notification.getTitle())
                    .description(notification.getDescription())
                    .userId(notification.getUserId())
                    .rewardItemName(notification.getRewardItem().getItemName())
                    .quantity(notification.getQuantity())
                    .locationName(notification.getLocation().getLocationName())
                    .status(notification.getStatus())
                    .isRead(notification.isRead())
                    .createdAt(notification.getCreatedAt())
                    .build();
            response.add(dto);
        }

        return response;
    }

    @Transactional
    public void markNotificationAsRead(Long notificationId) {
        RewardNotification notification = rewardNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.isRead()) {
            notification.setRead(true);
            rewardNotificationRepository.save(notification);
        }
    }
    public List<RewardOrderResponse> getUserRewardHistory(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<RewardOrder> orders = rewardOrderRepository.findByUserOrderByCreatedAtDesc(user);

        if (orders == null || orders.isEmpty()) {
            throw new RuntimeException("No orders found for user");
        }

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
    public void cancelOrderByUser(Long orderId) {
        RewardOrder order = rewardOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != RewardOrderStatus.PENDING && order.getStatus() != RewardOrderStatus.WAITING_FOR_USER)
            throw new RuntimeException("Order cannot be cancelled");

        Points points = pointRepository.findByUser(order.getUser())
                .orElseThrow(() -> new RuntimeException("Points not found"));
        long refund = (long) (order.getRewardItem().getPointCharge() * order.getQuantity());
        points.setPoint(points.getPoint() + refund);

        RewardItemLocation ril = rewardItemLocationRepository
                .findByRewardItemAndLocation(order.getRewardItem(), order.getLocation())
                .orElseThrow(() -> new RuntimeException("RewardItemLocation not found"));

        ril.setPending(ril.getPending() - order.getQuantity());

        order.setStatus(RewardOrderStatus.CANCELLED_BY_USER);

        pointRepository.save(points);
        rewardItemLocationRepository.save(ril);
        rewardOrderRepository.save(order);
    }
    @Transactional
    public void autoExpireOrders() {
        // Lấy tất cả các đơn hàng có trạng thái "WAITING_FOR_USER" và đã quá 3 ngày
        Timestamp threshold = Timestamp.valueOf(LocalDateTime.now().minusDays(3));
        List<RewardOrder> expiredOrders = rewardOrderRepository.findWaitingOrdersBefore(threshold);

        for (RewardOrder order : expiredOrders) {
            Points points = pointRepository.findByUser(order.getUser())
                    .orElseThrow(() -> new RuntimeException("Points not found"));
            long refund = (long) (order.getRewardItem().getPointCharge() * order.getQuantity());
            points.setPoint(points.getPoint() + refund);

            RewardItemLocation ril = rewardItemLocationRepository
                    .findByRewardItemAndLocation(order.getRewardItem(), order.getLocation())
                    .orElseThrow(() -> new RuntimeException("RewardItemLocation not found"));
            ril.setPending(ril.getPending() - order.getQuantity());

            order.setStatus(RewardOrderStatus.EXPIRED);  // Set status as EXPIRED

            // Lưu lại các thay đổi
            pointRepository.save(points);
            rewardItemLocationRepository.save(ril);
            rewardOrderRepository.save(order);
        }
    }
}
