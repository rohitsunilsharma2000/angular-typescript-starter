package com.example.blinkit.service;

import com.example.blinkit.dto.AdminDtos.MarkMissingRequest;
import com.example.blinkit.dto.AdminDtos.RiderStatusRequest;
import com.example.blinkit.entity.*;
import com.example.blinkit.exception.ApiException;
import com.example.blinkit.repository.*;
import com.example.blinkit.util.AuthUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpsService {
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final PickingTaskRepository pickingTaskRepository;
  private final PickingTaskItemRepository pickingTaskItemRepository;
  private final RiderRepository riderRepository;
  private final DeliveryBatchRepository deliveryBatchRepository;
  private final DeliveryAssignmentRepository deliveryAssignmentRepository;
  private final RefundRepository refundRepository;
  private final AuditLogRepository auditLogRepository;
  private final NotificationRepository notificationRepository;
  private final StreamService streamService;
  private final OrderService orderService;

  @Transactional
  public void assignRider(Long orderId, Long riderUserId) {
    Rider rider = riderRepository.findByUserId(riderUserId).orElseThrow(() -> new ApiException("Rider not found"));
    DeliveryBatch batch = deliveryBatchRepository.findByRiderIdAndStatus(rider.getId(), "ACTIVE").stream().findFirst()
        .orElseGet(() -> deliveryBatchRepository.save(DeliveryBatch.builder().riderId(rider.getId()).status("ACTIVE").createdAt(LocalDateTime.now()).build()));
    deliveryAssignmentRepository.save(DeliveryAssignment.builder().batchId(batch.getId()).orderId(orderId).accepted(false).build());
    Order order = orderRepository.findById(orderId).orElseThrow(() -> new ApiException("Order not found"));
    order.setRiderId(rider.getId());
    orderRepository.save(order);
    notifyUser(order.getUserId(), "Rider Assigned", "Rider assigned for order " + orderId);
    audit("ASSIGN_RIDER", "orders", String.valueOf(orderId));
    streamService.publishOrder(orderId, "RIDER_ASSIGNED", "Rider assigned");
  }

  @Transactional
  public void ensurePickingTask(Order order) {
    pickingTaskRepository.findByOrderId(order.getId()).orElseGet(() -> {
      PickingTask task = pickingTaskRepository.save(PickingTask.builder().orderId(order.getId()).status("PICKING").createdAt(LocalDateTime.now()).build());
      orderItemRepository.findByOrderId(order.getId()).forEach(i ->
          pickingTaskItemRepository.save(PickingTaskItem.builder().pickingTaskId(task.getId()).orderItemId(i.getId()).missingQty(0).pickedQty(0).build()));
      return task;
    });
  }

  public List<Order> pickingQueue() {
    return orderRepository.findAll().stream().filter(o -> o.getStatus() == OrderStatus.PICKING).toList();
  }

  public List<OrderItem> picklist(Long orderId) {
    return orderItemRepository.findByOrderId(orderId);
  }

  @Transactional
  public void markMissing(Long orderId, MarkMissingRequest req) {
    OrderItem item = orderItemRepository.findById(req.orderItemId()).orElseThrow(() -> new ApiException("Order item not found"));
    if (!item.getOrderId().equals(orderId)) {
      throw new ApiException("Item does not belong to order");
    }
    item.setRefundedQty(item.getRefundedQty() + req.missingQty());
    orderItemRepository.save(item);
    BigDecimal amount = item.getPrice().multiply(BigDecimal.valueOf(req.missingQty()));
    refundRepository.save(Refund.builder()
        .orderId(orderId)
        .orderItemId(item.getId())
        .amount(amount)
        .status(RefundStatus.PENDING)
        .destination("WALLET")
        .createdAt(LocalDateTime.now())
        .build());
    audit("MARK_MISSING", "order_items", String.valueOf(item.getId()));
    streamService.publishOrder(orderId, "MISSING_MARKED", "Missing item marked");
  }

  @Transactional
  public void markPacked(Long orderId) {
    orderService.advanceStatus(orderId, OrderStatus.PACKED);
    audit("MARK_PACKED", "orders", String.valueOf(orderId));
  }

  public List<DeliveryBatch> riderBatches(String status) {
    Rider rider = riderRepository.findByUserId(AuthUtil.userId()).orElseThrow(() -> new ApiException("Rider profile missing"));
    return deliveryBatchRepository.findByRiderIdAndStatus(rider.getId(), status == null ? "ACTIVE" : status);
  }

  @Transactional
  public void riderAcceptOrder(Long orderId) {
    List<DeliveryAssignment> assignments = deliveryAssignmentRepository.findAll().stream().filter(a -> a.getOrderId().equals(orderId)).toList();
    if (assignments.isEmpty()) {
      throw new ApiException("Assignment not found");
    }
    DeliveryAssignment assignment = assignments.get(0);
    assignment.setAccepted(true);
    deliveryAssignmentRepository.save(assignment);
    streamService.publishOrder(orderId, "RIDER_ACCEPTED", "Rider accepted order");
  }

  @Transactional
  public void riderStatus(Long orderId, RiderStatusRequest req) {
    if ("PICKED_UP".equals(req.eventType())) {
      orderService.advanceStatus(orderId, OrderStatus.PICKED_UP);
    }
    if ("OUT_FOR_DELIVERY".equals(req.eventType())) {
      orderService.advanceStatus(orderId, OrderStatus.OUT_FOR_DELIVERY);
    }
    if ("DELIVERED".equals(req.eventType())) {
      orderService.advanceStatus(orderId, OrderStatus.DELIVERED);
    }
    orderService.pushEvent(orderId, req.eventType(), req.message());
    streamService.publishOrder(orderId, req.eventType(), req.message());
  }

  public List<Notification> notifications() {
    return notificationRepository.findByUserIdOrderByIdDesc(AuthUtil.userId());
  }

  @Transactional
  public void readNotification(Long id) {
    Notification n = notificationRepository.findById(id).orElseThrow(() -> new ApiException("Notification not found"));
    n.setReadFlag(true);
    notificationRepository.save(n);
  }

  public List<AuditLog> audits() {
    return auditLogRepository.findAll();
  }

  public void notifyUser(Long userId, String title, String body) {
    notificationRepository.save(Notification.builder()
        .userId(userId)
        .title(title)
        .body(body)
        .readFlag(false)
        .createdAt(LocalDateTime.now())
        .build());
  }

  public void audit(String action, String entityType, String entityId) {
    auditLogRepository.save(AuditLog.builder()
        .actorUserId(AuthUtil.userId())
        .action(action)
        .entityType(entityType)
        .entityId(entityId)
        .payload("{}")
        .createdAt(LocalDateTime.now())
        .build());
  }
}
