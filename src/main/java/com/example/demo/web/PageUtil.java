package com.example.demo.web;

import com.example.demo.dto.PageResponse;
import org.springframework.data.domain.Page;

public class PageUtil {
  public static <T> PageResponse<T> wrap(Page<T> page){
    return new PageResponse<>(
        page.getContent(),
        new PageResponse.Meta(page.getTotalElements(), page.getNumber()+1, page.getTotalPages())
    );
  }
}
