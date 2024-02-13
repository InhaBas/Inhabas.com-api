package com.inhabas.api.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.domain.contest.domain.valueObject.ContestType;

public class ContestTypeConverter {

  @Component
  public static class StringToContestTypeConverter implements Converter<String, ContestType> {

    // Enum형 ContestType의 각 요소들 (CONTEST, EXTERNAL_ACTIVITY..)을 소문자 문자열로 변환시켜 url에 매핑시키기 위한 컨버터
    @Override
    public ContestType convert(String source) {
      if (!StringUtils.hasText(source)) {
        throw new InvalidInputException();
      }
      try {
        return ContestType.valueOf(source.trim().toUpperCase());
      } catch (IllegalArgumentException e) {
        throw new NotFoundException();
      }
    }
  }

  @Component
  public static class ContestTypeToStringConverter implements Converter<ContestType, String> {
    @Override
    public String convert(ContestType source) {
      return source != null ? source.toString().toLowerCase() : null;
    }
  }
}
