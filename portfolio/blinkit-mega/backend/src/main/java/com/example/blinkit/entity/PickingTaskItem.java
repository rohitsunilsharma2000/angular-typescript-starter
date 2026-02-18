package com.example.blinkit.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "picking_task_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PickingTaskItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "picking_task_id")
  private Long pickingTaskId;
  @Column(name = "order_item_id")
  private Long orderItemId;
  @Column(name = "missing_qty")
  private Integer missingQty;
  @Column(name = "picked_qty")
  private Integer pickedQty;
}
