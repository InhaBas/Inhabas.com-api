package com.inhabas.api.dto.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateBoardDtoTest {

    @DisplayName("UpdateBoardDto를 정상적으로 생성한다. ")
    @Test
    public void UpdateBoardDto_is_OK() {
        //given
        Integer id = 1;
        String title = "이것은 제목";
        String contents = "이것은 내용입니다.";
        Integer category_id = 2;

        // when
        UpdateBoardDto updateBoardDto = new UpdateBoardDto();
        updateBoardDto.setId(id);
        updateBoardDto.setTitle(title);
        updateBoardDto.setContents(contents);
        updateBoardDto.setCategory_id(category_id);

        // then
        assertThat(updateBoardDto.getTitle()).isEqualTo(title);
        assertThat(updateBoardDto.getContents()).isEqualTo(contents);
        assertThat(updateBoardDto.getCategory_id()).isEqualTo(category_id);

    }
}
