package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StudentIdTest {

    @DisplayName("StudentId 타입에 유저 이름 저장")
    @Test
    public void StudentId_is_OK() {
        //given
        Integer id = 12171707;

        //when
        StudentId studentId = new StudentId(id);

        //then
        assertThat(studentId.getValue()).isEqualTo(12171707);
    }

    @DisplayName("StudentId 타입에 잘못된 StudentId 저장 시도. 음수")
    @Test
    public void UserName_is_too_long() {
        //given
        Integer id = -1;

        //when
        assertThrows(
                IllegalArgumentException.class,
                ()->  new StudentId(id)
        );
    }

    @DisplayName("학번은 null 일 수 없다.")
    @Test
    public void Username_cannot_be_null() {
        assertThrows(IllegalArgumentException.class,
                ()-> new StudentId(null));
    }

}
