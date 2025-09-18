package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity @Table(name="tasks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Task {
  @Id @GeneratedValue
  private UUID id;

  @NotBlank
  @Column(nullable = false)
  private String title;

  private String description;
  private String category;
  private boolean done;

  @CreationTimestamp
  @Column(updatable = false)
  private Instant createdAt;

  @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Subtask> subtasks = new ArrayList<>();
}
