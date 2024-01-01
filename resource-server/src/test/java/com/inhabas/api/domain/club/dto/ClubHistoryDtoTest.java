package com.inhabas.api.domain.club.dto;

import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

class ClubHistoryDtoTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @DisplayName("Id, writerId 가 Positive 가 아니면 validation 실패")
    @Test
    void Positive_Test() {
        //given
        ClubHistoryDto clubHistoryDto = ClubHistoryDto.builder()
                .id(-1L)
                .title(new Title("goodTitle"))
                .content(new Content("goodContent"))
                .writerId(-1L)
                .dateHistory(LocalDateTime.now())
                .build();

        //when
        Set<ConstraintViolation<ClubHistoryDto>> violations = validator.validate(clubHistoryDto);

        //then
        Assertions.assertThat(violations.size()).isEqualTo(2);

    }

    @DisplayName("Id, writerId, dateHistory 가 null 이면 validation 실패")
    @Test
    void NotNull_Test() {
        //given
        ClubHistoryDto clubHistoryDto = ClubHistoryDto.builder()
                .id(null)
                .title(new Title("goodTitle"))
                .content(new Content("goodContent"))
                .writerId(null)
                .dateHistory(null)
                .build();

        //when
        Set<ConstraintViolation<ClubHistoryDto>> violations = validator.validate(clubHistoryDto);

        //then
        Assertions.assertThat(violations.size()).isEqualTo(3);

    }

}