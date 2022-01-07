package com.inhabas.api.domain.wrapper;

import com.inhabas.api.domain.file.type.wrapper.FilePath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilepathTest {

    @DisplayName("FilePath 타입에 파일 경로를 저장한다.")
    @Test
    public void FilePath_is_OK() {
        //given
        String path = "/file/board/";
        String filename = "file.txt";

        //when
        FilePath filePath = new FilePath(path + filename);

        //then
        assertThat(filePath.getValue()).isEqualTo("/file/board/file.txt");
    }

    @DisplayName("FilePath 타입에 너무 긴 경로 저장을 시도한다.")
    @Test
    public void FilePath_is_too_long() {
        //given
        String path = "/" + "file".repeat(300);
        String tooLongFilePath = path + "file.txt";

        //when
        assertThrows(IllegalArgumentException.class,
                () -> new FilePath(tooLongFilePath));
    }

    @DisplayName("FilePath 는 null이 될 수 없다.")
    @Test
    public void FilePath_cannot_be_null() {
        assertThrows(IllegalArgumentException.class,
                () -> new FilePath(null));
    }

    @DisplayName("FilePath 는 빈 문자열이 될 수 없다.")
    @Test
    public void FilePath_cannot_be_blank() {
        assertThrows(IllegalArgumentException.class,
                () -> new FilePath("\n\t"));
    }
}
