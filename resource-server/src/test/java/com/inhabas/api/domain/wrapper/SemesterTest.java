package com.inhabas.api.domain.wrapper;

import com.inhabas.api.domain.member.type.wrapper.Semester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SemesterTest {

    @DisplayName("Semester 타입에 학기를 지정")
    @Test
    public void Semester_is_OK() {
        //given
        Semester firstSemester = new Semester(1);
        Semester secondSemester = new Semester(2);

        //then
        assertThat(firstSemester.getValue()).isEqualTo(1);
        assertThat(secondSemester.getValue()).isEqualTo(2);
    }

    @DisplayName("존재하지 않는 학기를 지정")
    @Test
    public void No_Such_Semester() {
        //when
        assertThrows(
                IllegalArgumentException.class,
                () -> new Semester(3)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new Semester(0)
        );
    }
}
