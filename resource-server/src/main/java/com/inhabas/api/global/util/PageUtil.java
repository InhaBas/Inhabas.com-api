package com.inhabas.api.global.util;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Pageable;

public class PageUtil {

  public static <T> List<T> getPagedDtoList(Pageable pageable, List<T> dtoList) {

    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), dtoList.size());

    // 시작 인덱스가 리스트 크기보다 크거나 같은 경우, 빈 리스트 반환
    if (start >= dtoList.size()) {
      return Collections.emptyList();
    }

    return dtoList.subList(start, end);
  }
}
