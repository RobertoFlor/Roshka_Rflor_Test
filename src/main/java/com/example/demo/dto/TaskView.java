package com.example.demo.dto;

import com.example.demo.model.Task;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class TaskView {
  private UUID id;
  private String title;
  private String description;
  private String category;
  private boolean done;
  private Instant createdAt;

  public static TaskView from(Task t){
    TaskView v = new TaskView();
    v.setId(t.getId());
    v.setTitle(t.getTitle());
    v.setDescription(t.getDescription());
    v.setCategory(t.getCategory());
    v.setDone(t.isDone());
    v.setCreatedAt(t.getCreatedAt());
    return v;
  }
}
