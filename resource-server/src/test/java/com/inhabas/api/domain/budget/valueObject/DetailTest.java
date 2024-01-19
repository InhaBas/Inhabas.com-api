package com.inhabas.api.domain.budget.valueObject;

import com.inhabas.api.domain.budget.domain.valueObject.Details;
import com.inhabas.api.domain.budget.domain.valueObject.RejectReason;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DetailTest {

    @DisplayName("Details 타입에 문자열을 저장한다.")
    @Test
    public void normalDetailsTest() {
        //given
        String  detailsString = "예산 내역 상세 내용입니다.";

        //when
        Details details = new Details(detailsString);

        //then
        assertThat(details.getValue()).isEqualTo("예산 내역 상세 내용입니다.");
    }

    @DisplayName("Details 타입에 너무 긴 문자열을 저장한다. 300자 이상")
    @Test
    public void tooLongDetailsTest() {
        //given
        String detailsString = "지금이문장은10자임".repeat(30);

        //then
        Assertions.assertThatThrownBy(() -> new Details(detailsString))
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessage(null);
    }

    @DisplayName("예산 내역 제목은 null 일 수 없습니다.")
    @Test
    public void detailsCannotBeNullTest() {

        Assertions.assertThatThrownBy(() -> new Details(null))
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessage(null);
    }

    @DisplayName("예산 상세내역 은 빈 문자열일 수 없습니다.")
    @Test
    public void detailsCannotBeBlankTest() {

        Assertions.assertThatThrownBy(() -> new Details("\t"))
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessage(null);
    }
}
