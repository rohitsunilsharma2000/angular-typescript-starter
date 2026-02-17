package com.example.classroom.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "periods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Period {
  @Id
  private String id;

  @Column(nullable = false)
  private String classroomId;

  @Column(name = "period_day", nullable = false)
  private String day;

  @Column(nullable = false)
  private String subject;

  @Column(nullable = false)
  private String startTime;

  @Column(nullable = false)
  private String endTime;
}
