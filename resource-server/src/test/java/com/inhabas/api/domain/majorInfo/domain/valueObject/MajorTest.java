package com.inhabas.api.domain.majorInfo.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MajorTest {

    @DisplayName("Major 타입에 제목을 저장한다.")
    @Test
    public void Major_is_OK() {

        //given
        String  majorString = "사회과학대학";

        //when
        Major major = new Major(majorString);

        //then
        assertThat(major.getValue()).isEqualTo("사회과학대학");
    }

    @DisplayName("Major 타입에 너무 긴 이름을 저장한다. 50자 이상")
    @Test
    public void Major_is_too_long() {

        //given
        String majorString = "지금이문장은10자임".repeat(50);

        //then
        assertThrows(IllegalArgumentException.class,
                () -> new Major(majorString));
    }

    @DisplayName("Major 은 null 일 수 없습니다.")
    @Test
    public void Major_cannot_be_Null() {

        assertThrows(IllegalArgumentException.class,
                () -> new Major(null));
    }

    @DisplayName("Major 은 빈 문자열일 수 없습니다.")
    @Test
    public void Major_cannot_be_Blank() {

        assertThrows(IllegalArgumentException.class,
                () -> new Major("\t"));
    }
}
