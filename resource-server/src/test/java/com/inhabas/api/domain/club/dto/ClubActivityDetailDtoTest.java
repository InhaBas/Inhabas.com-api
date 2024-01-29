package com.inhabas.api.domain.club.dto;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ClubActivityDetailDtoTest {

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


    @DisplayName("DTO validation 검사")
    @Test
    public void validationTest() {
        //given
        ClubActivityDetailDto clubActivityDetailDto = ClubActivityDetailDto.builder()
                .id(-1L)
                .title("")
                .content("")
                .writerName("")
                .dateCreated(null)
                .dateUpdated(null)
                .files(null)
                .build();

        //when
        Set<ConstraintViolation<ClubActivityDetailDto>> violations = validator.validate(clubActivityDetailDto);

        //then
        final int allField = 7;
        assertThat(violations).hasSize(allField);

    }

}