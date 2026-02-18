package com.example.blinkit.repository;

import com.example.blinkit.entity.Refund;
import com.example.blinkit.entity.RefundStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long> {
  List<Refund> findByStatus(RefundStatus status);
}
