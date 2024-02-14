package com.inhabas.api.domain.normalBoard.dto;

import com.inhabas.api.domain.file.dto.FileDownloadDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NormalBoardDetailDtoTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private final List<FileDownloadDto> emptyList = new ArrayList<>();

    @BeforeAll
    public static void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @DisplayName("NormalBoardDetailDto 객체를 정상적으로 생성한다.")
    @Test
    public void NormalBoardDetailDto_is_OK() {
        // given
        NormalBoardDetailDto normalBoardDetailDto =
                new NormalBoardDetailDto(1L, "title", "content", "writer",
                        LocalDateTime.now(), LocalDateTime.now(), emptyList, false);

        // when
        Set<ConstraintViolation<NormalBoardDetailDto>> violations = validator.validate(normalBoardDetailDto);

        // then
        assertTrue(violations.isEmpty());
    }

    @DisplayName("normalBoardDetailDto title 필드가 null 이면 validation 실패")
    @Test
    public void Title_is_null() {
        // given
        NormalBoardDetailDto normalBoardDetailDto =
                new NormalBoardDetailDto(1L, null, "content", "writer",
                        LocalDateTime.now(), LocalDateTime.now(), emptyList, false);

        // when
        Set<ConstraintViolation<NormalBoardDetailDto>> violations = validator.validate(normalBoardDetailDto);

        // then
        assertEquals(1, violations.size());
    }
}