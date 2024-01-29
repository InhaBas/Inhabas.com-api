package com.inhabas.api.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.inhabas.api.domain.menu.domain.valueObject.MenuId;

public class MenuIdConverter {

  @Component
  public static class StringToMenuIdConverter implements Converter<String, MenuId> {
    @Override
    public MenuId convert(String source) {
      return new MenuId(Integer.parseInt(source));
    }
  }

  @Component
  public static class MenuIdToStringConverter implements Converter<MenuId, String> {
    @Override
    public String convert(MenuId source) {
      return source.toString();
    }
  }
}
