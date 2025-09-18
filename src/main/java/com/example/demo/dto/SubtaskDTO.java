package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubtaskDTO {
  @NotBlank private String title;
  private Boolean done;
}
