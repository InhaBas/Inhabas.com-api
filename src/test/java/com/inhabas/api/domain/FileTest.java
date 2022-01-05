package com.inhabas.api.domain;


import com.inhabas.api.domain.file.type.wrapper.FileName;

import com.inhabas.api.domain.file.type.wrapper.FilePath;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileTest {

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
        assertThrows(
                IllegalArgumentException.class,
                () -> { FileName fileName = new FileName(tooLongFileName); }
        );
    }

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
        assertThrows(
                IllegalArgumentException.class,
                ()->{FilePath filePath = new FilePath(tooLongFilePath);}
        );
    }
}
