package com.example.demo.repository;

import com.example.demo.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
  Page<Task> findByCategoryIgnoreCase(String category, Pageable pageable);
}
