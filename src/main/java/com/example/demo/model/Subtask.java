package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity @Table(name="subtasks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Subtask {
  @Id @GeneratedValue
  private UUID id;

  @NotBlank
  @Column(nullable = false)
  private String title;

  private boolean done;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "task_id", nullable = false)
  private Task task;
}
