package com.inhabas.api.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.domain.project.domain.ProjectBoardType;

public class ProjectBoardTypeConverter {

  @Component
  public static class StringToProjectBoardTypeConverter
      implements Converter<String, ProjectBoardType> {

    @Override
    public ProjectBoardType convert(String source) {
      if (!StringUtils.hasText(source)) {
        throw new InvalidInputException();
      }
      try {
        return ProjectBoardType.valueOf(source.trim().toUpperCase());
      } catch (IllegalArgumentException e) {
        throw new NotFoundException();
      }
    }
  }

  @Component
  public static class ProjectBoardTypeToStringConverter
      implements Converter<ProjectBoardType, String> {
    @Override
    public String convert(ProjectBoardType source) {
      return source != null ? source.toString().toLowerCase() : null;
    }
  }
}
