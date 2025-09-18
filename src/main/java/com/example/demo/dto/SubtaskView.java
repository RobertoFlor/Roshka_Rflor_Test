package com.example.demo.dto;

import com.example.demo.model.Subtask;
import lombok.Data;

import java.util.UUID;

@Data
public class SubtaskView {
  private UUID id;
  private String title;
  private boolean done;

  public static SubtaskView from(Subtask s){
    SubtaskView v = new SubtaskView();
    v.setId(s.getId());
    v.setTitle(s.getTitle());
    v.setDone(s.isDone());
    return v;
  }
}
