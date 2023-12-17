package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GradeTest {

    @DisplayName("Grade 타입에 기수를 지정")
    @Test
    public void Grade_is_OK() {
        //given
        Grade normalGrade = new Grade(1);
        Grade nullGrade = new Grade();

        //then
        assertThat(normalGrade.getValue()).isEqualTo(1);
        assertThat(nullGrade.getValue()).isEqualTo(0);
    }

    @DisplayName("존재할 수 없는 Grade 지정")
    @Test
    public void No_Such_Grade() {
        //when
        assertThrows(
                IllegalArgumentException.class,
                () -> new Grade(-1)
        );
    }
}
