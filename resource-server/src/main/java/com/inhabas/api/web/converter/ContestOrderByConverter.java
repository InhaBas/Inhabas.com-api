package com.inhabas.api.web.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.domain.contest.domain.valueObject.OrderBy;

public class ContestOrderByConverter {

  @Component
  public static class StringToOrderByConverter implements Converter<String, OrderBy> {

    // Enum형 OrderBy의 요소들 DATE_CREATED, DATE_CONTEST_END ... 를 문자열 형태로 변환
    @Override
    public OrderBy convert(String source) {
      if (!StringUtils.hasText(source)) {
        throw new InvalidInputException();
      }

      String enumName = source.trim().toUpperCase().replaceAll("([a-z])([A-Z]+)", "$1_$2");

      try {
        return OrderBy.valueOf(enumName);
      } catch (IllegalArgumentException e) {
        throw new NotFoundException();
      }
    }
  }

  @Component
  public static class OrderByToStringConverter implements Converter<OrderBy, String> {
    @Override
    public String convert(OrderBy source) {
      if (source == null) {
        return null;
      }

      String name = source.name();
      Matcher matcher = Pattern.compile("_(.)").matcher(name);
      StringBuffer sb = new StringBuffer();

      while (matcher.find()) {
        matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
      }
      matcher.appendTail(sb);

      String camelCaseResult = sb.toString();
      camelCaseResult =
          Character.toLowerCase(camelCaseResult.charAt(0)) + camelCaseResult.substring(1);

      return camelCaseResult;
    }
  }
}
