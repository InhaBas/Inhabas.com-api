package com.inhabas.api.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.domain.scholarship.domain.ScholarshipBoardType;

public class ScholarshipBoardConverter {

  @Component
  public static class StringToScholarshipTypeConverter
      implements Converter<String, ScholarshipBoardType> {

    @Override
    public ScholarshipBoardType convert(String source) {
      if (!StringUtils.hasText(source)) {
        throw new InvalidInputException();
      }
      try {
        return ScholarshipBoardType.valueOf(source.trim().toUpperCase());
      } catch (IllegalArgumentException e) {
        throw new NotFoundException();
      }
    }
  }

  @Component
  public static class ScholarshipBoardTypeToStringConverter
      implements Converter<ScholarshipBoardType, String> {
    @Override
    public String convert(ScholarshipBoardType source) {
      return source != null ? source.toString().toLowerCase() : null;
    }
  }
}
