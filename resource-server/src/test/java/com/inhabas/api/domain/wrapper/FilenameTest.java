package com.inhabas.api.domain.wrapper;

import com.inhabas.api.domain.file.type.wrapper.FileName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilenameTest {

    @DisplayName("FileName 타입에 파일 이름을 저장한다.")
    @Test
    public void FileName_is_OK() {
        //when
        FileName fileName = new FileName("filename_test_file.txt");

        //then
        assertThat(fileName.getValue()).isEqualTo("filename_test_file.txt");
        assertThat(fileName.getExtension()).isEqualTo("txt");
        assertThat(fileName.getNameWithoutExtension()).isEqualTo("filename_test_file");
    }

    @DisplayName("FileName 타입에 너무 긴 파일 이름 저장을 시도한다.")
    @Test
    public void FileName_is_too_long() {
        //given
        String tooLongFileName = "a".repeat(500) + "txt";

        //when
        assertThrows(IllegalArgumentException.class,
                () -> new FileName(tooLongFileName));
    }

    @DisplayName("FileName 에 null 은 허용 안된다.")
    @Test
    public void FileName_cannot_be_null() {
        assertThrows(IllegalArgumentException.class,
                () -> new FileName(null));
    }

    @DisplayName("FileName 이 빈 문자열이면 안된다.")
    @Test
    public void FileName_cannot_be_blank_string() {
        assertThrows(IllegalArgumentException.class,
                () -> new FileName("    "));
    }
}
