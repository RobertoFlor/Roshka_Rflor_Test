package com.example.demo.repository;

import com.example.demo.model.Subtask;
import com.example.demo.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubtaskRepository extends JpaRepository<Subtask, UUID> {
  Page<Subtask> findByTask(Task task, Pageable pageable);
}
