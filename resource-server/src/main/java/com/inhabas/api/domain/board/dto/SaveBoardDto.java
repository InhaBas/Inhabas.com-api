package com.inhabas.api.domain.board.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveBoardDto {
  @NotBlank(message = "제목을 입력하세요.")
  @Length(max = 100, message = "제목은 최대 100자입니다.")
  private String title;

  @NotBlank(message = "본문을 입력하세요.")
  private String contents;

  @NotNull private MenuId menuId;

  public SaveBoardDto(String title, String contents, MenuId menuId) {
    this.title = title;
    this.contents = contents;
    this.menuId = menuId;
  }
}
