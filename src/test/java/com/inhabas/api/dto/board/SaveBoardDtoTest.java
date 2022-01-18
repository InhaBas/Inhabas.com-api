package com.inhabas.api.dto.board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SaveBoardDtoTest {

    @DisplayName("SaveBoardDto 객체를 정상적으로 생성한다.")
    @Test
    public void SaveBoardDto_is_OK(){
        //given
        String title = "이것은 제목";
        String contents = "이것은 내용입니다.";
        Integer category_id = 2;

        //when
        SaveBoardDto saveBoardDto = new SaveBoardDto();
        saveBoardDto.setTitle(title);
        saveBoardDto.setContents(contents);
        saveBoardDto.setCategory_id(category_id);


        // then
        assertThat(saveBoardDto.getTitle()).isEqualTo(title);
        assertThat(saveBoardDto.getContents()).isEqualTo(contents);
        assertThat(saveBoardDto.getCategory_id()).isEqualTo(category_id);
    }

    @DisplayName("SaveBoardDto의 contents 필드가 null 상태이다. ")
    @Test
    public void Contents_is_null() {
        String title = "이것은 제목";
        String contents = null;
        Integer category_id = 2;

        // when
        SaveBoardDto saveBoardDto = new SaveBoardDto();
        saveBoardDto.setTitle(title);
        saveBoardDto.setContents(contents);
        saveBoardDto.setCategory_id(category_id);

        // then
        assertThat(saveBoardDto.getTitle()).isEqualTo(title);
        assertThat(saveBoardDto.getContents()).isEqualTo(contents);
        assertThat(saveBoardDto.getCategory_id()).isEqualTo(category_id);
    }


}
