package com.example.demo.web;

import com.example.demo.dto.*;
import com.example.demo.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final TaskService service;

  // POST /tasks
  @PostMapping
  public ResponseEntity<TaskView> create(@Valid @RequestBody TaskDTO dto){
    TaskView created = service.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  // GET /tasks (page, limit, sortBy, order, category)
  @GetMapping
  public PageResponse<TaskView> list(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "desc") String order,
      @RequestParam(required = false) String category
  ){
    Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(Math.max(0, page-1), Math.min(limit, 100), sort);
    return PageUtil.wrap(service.list(category, pageable));
  }

  // GET /tasks/:id
  @GetMapping("/{id}")
  public TaskView get(@PathVariable UUID id){ return service.get(id); }

  // PUT /tasks/:id
  @PutMapping("/{id}")
  public TaskView update(@PathVariable UUID id, @Valid @RequestBody TaskDTO dto){
    return service.update(id, dto);
  }

  // DELETE /tasks/:id
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id){
    service.delete(id);
    return ResponseEntity.ok().build(); // 200
  }

  // --- Subtasks anidadas ---

  // POST /tasks/:id/subtasks
  @PostMapping("/{taskId}/subtasks")
  public ResponseEntity<SubtaskView> createSub(@PathVariable UUID taskId, @Valid @RequestBody SubtaskDTO dto){
    var created = service.createSubtask(taskId, dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  // GET /tasks/:id/subtasks
  @GetMapping("/{taskId}/subtasks")
  public PageResponse<SubtaskView> listSub(
      @PathVariable UUID taskId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int limit,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "asc") String order
  ){
    Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(Math.max(0, page-1), Math.min(limit, 100), sort);
    return PageUtil.wrap(service.listSubtasks(taskId, pageable));
  }

  // GET /tasks/:id/subtasks/:id
  @GetMapping("/{taskId}/subtasks/{subId}")
  public SubtaskView getSub(@PathVariable UUID taskId, @PathVariable UUID subId){
    return service.getSubtask(taskId, subId);
  }

  // PUT /tasks/:id/subtasks/:id
  @PutMapping("/{taskId}/subtasks/{subId}")
  public SubtaskView updateSub(@PathVariable UUID taskId, @PathVariable UUID subId, @Valid @RequestBody SubtaskDTO dto){
    return service.updateSubtask(taskId, subId, dto);
  }

  // DELETE /tasks/:id/subtasks/:id
  @DeleteMapping("/{taskId}/subtasks/{subId}")
  public ResponseEntity<Void> delSub(@PathVariable UUID taskId, @PathVariable UUID subId){
    service.deleteSubtask(taskId, subId);
    return ResponseEntity.ok().build();
  }
}
