package com.inhabas.api.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.domain.contest.domain.valueObject.OrderBy;

public class ContestOrderByConverter {

  @Component
  public static class StringToOrderByConverter implements Converter<String, OrderBy> {

    // Enum형 OrderBy의 요소들 ALL, DUE_DATE ... 를 문자열 형태로 변환
    @Override
    public OrderBy convert(String source) {
      if (!StringUtils.hasText(source)) {
        throw new InvalidInputException();
      }
      try {
        return OrderBy.valueOf(source.trim().toUpperCase());
      } catch (IllegalArgumentException e) {
        throw new NotFoundException();
      }
    }
  }

  @Component
  public static class OrderByToStringConverter implements Converter<OrderBy, String> {

    @Override
    public String convert(OrderBy source) {
      return source != null ? source.toString().toLowerCase() : null;
    }
  }
}
