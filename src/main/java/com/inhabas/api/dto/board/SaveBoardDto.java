package com.inhabas.api.dto.board;

import com.inhabas.api.domain.board.NormalBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
@NoArgsConstructor
public class SaveBoardDto {
    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 100, message = "제목은 최대 100자입니다.")
    private String title;

    @NotBlank(message = "본문을 입력하세요.")
    private String contents;

    @NotNull(message = "카테고리를 선택하세요.")
    private Integer categoryId;

    public NormalBoard toEntity() {
        return null;
    }
}
