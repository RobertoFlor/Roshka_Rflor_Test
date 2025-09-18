package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data @AllArgsConstructor
public class PageResponse<T> {
  private List<T> data;
  private Meta meta;

  @Data @AllArgsConstructor
  public static class Meta {
    private long total;
    private int page;
    private int totalPages;
  }
}
