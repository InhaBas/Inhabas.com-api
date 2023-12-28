package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GenerationTest {

    @DisplayName("Generation 타입에 기수를 지정")
    @Test
    public void Semester_is_OK() {
        //given
        Generation firstGeneration = new Generation(1);
        Generation secondGeneration = new Generation(2);

        //then
        assertThat(firstGeneration.getValue()).isEqualTo(1);
        assertThat(secondGeneration.getValue()).isEqualTo(2);
    }

    @DisplayName("존재할 수 없는 기수를 지정")
    @Test
    public void No_Such_Semester() {
        //when
        assertThrows(
                InvalidInputException.class,
                () -> new Generation(-1)
        );
        assertThrows(
                InvalidInputException.class,
                () -> new Generation(0)
        );
    }
}
