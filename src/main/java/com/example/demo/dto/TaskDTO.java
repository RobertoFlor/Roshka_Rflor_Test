package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskDTO {
  @NotBlank private String title;
  private String description;
  private String category;
  private Boolean done;
}
