package com.example.demo.service;

import com.example.demo.dto.SubtaskDTO;
import com.example.demo.dto.SubtaskView;
import com.example.demo.dto.TaskDTO;
import com.example.demo.dto.TaskView;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Subtask;
import com.example.demo.model.Task;
import com.example.demo.repository.SubtaskRepository;
import com.example.demo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service @RequiredArgsConstructor
public class TaskService {
  private final TaskRepository taskRepo;
  private final SubtaskRepository subtaskRepo;

  public TaskView create(TaskDTO dto){
    Task t = Task.builder()
      .title(dto.getTitle())
      .description(dto.getDescription())
      .category(dto.getCategory())
      .done(Boolean.TRUE.equals(dto.getDone()))
      .build();
    return TaskView.from(taskRepo.save(t));
  }

  public Page<TaskView> list(String category, Pageable pageable){
    Page<Task> p = (category==null || category.isBlank())
        ? taskRepo.findAll(pageable)
        : taskRepo.findByCategoryIgnoreCase(category, pageable);
    return p.map(TaskView::from);
  }

  public TaskView get(UUID id){
    Task t = taskRepo.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
    return TaskView.from(t);
  }

  public TaskView update(UUID id, TaskDTO dto){
    Task t = taskRepo.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));
    if(dto.getTitle()!=null) t.setTitle(dto.getTitle());
    if(dto.getDescription()!=null) t.setDescription(dto.getDescription());
    if(dto.getCategory()!=null) t.setCategory(dto.getCategory());
    if(dto.getDone()!=null) t.setDone(dto.getDone());
    return TaskView.from(taskRepo.save(t));
  }

  public void delete(UUID id){
    if(!taskRepo.existsById(id)) throw new NotFoundException("Task not found");
    taskRepo.deleteById(id);
  }

  // Subtasks
  private Task loadTask(UUID taskId){
    return taskRepo.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found"));
  }

  public SubtaskView createSubtask(UUID taskId, SubtaskDTO dto){
    Task t = loadTask(taskId);
    Subtask s = Subtask.builder()
        .task(t)
        .title(dto.getTitle())
        .done(Boolean.TRUE.equals(dto.getDone()))
        .build();
    return SubtaskView.from(subtaskRepo.save(s));
  }

  public Page<SubtaskView> listSubtasks(UUID taskId, Pageable pageable){
    Task t = loadTask(taskId);
    return subtaskRepo.findByTask(t, pageable).map(SubtaskView::from);
  }

  public SubtaskView getSubtask(UUID taskId, UUID subId){
    Task t = loadTask(taskId);
    Subtask s = subtaskRepo.findById(subId)
        .filter(ss -> ss.getTask().getId().equals(t.getId()))
        .orElseThrow(() -> new NotFoundException("Subtask not found"));
    return SubtaskView.from(s);
  }

  public SubtaskView updateSubtask(UUID taskId, UUID subId, SubtaskDTO dto){
    Task t = loadTask(taskId);
    Subtask s = subtaskRepo.findById(subId)
        .filter(ss -> ss.getTask().getId().equals(t.getId()))
        .orElseThrow(() -> new NotFoundException("Subtask not found"));
    if(dto.getTitle()!=null) s.setTitle(dto.getTitle());
    if(dto.getDone()!=null) s.setDone(dto.getDone());
    return SubtaskView.from(subtaskRepo.save(s));
  }

  public void deleteSubtask(UUID taskId, UUID subId){
    Task t = loadTask(taskId);
    Subtask s = subtaskRepo.findById(subId)
        .filter(ss -> ss.getTask().getId().equals(t.getId()))
        .orElseThrow(() -> new NotFoundException("Subtask not found"));
    subtaskRepo.delete(s);
  }
}
