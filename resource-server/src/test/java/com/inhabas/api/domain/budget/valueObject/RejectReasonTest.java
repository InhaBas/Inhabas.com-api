package com.inhabas.api.domain.budget.valueObject;

import com.inhabas.api.domain.budget.domain.valueObject.Price;
import com.inhabas.api.domain.budget.domain.valueObject.RejectReason;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

public class RejectReasonTest {

    @DisplayName("RejectReason 타입에 문자열을 저장한다.")
    @Test
    public void normalRejectReasonTest() {
        //given
        String  rejectString = "거부이유입니다.";

        //when
        RejectReason rejectReason = new RejectReason(rejectString);

        //then
        Assertions.assertThat(rejectReason.getValue()).isEqualTo("거부이유입니다.");
    }

    @DisplayName("RejectReason 타입에 너무 긴 문자열을 저장한다. 200자 이상")
    @Test
    public void tooLongRejectReasonTest() {
        //given
        String rejectString = "지금이문장은10자임".repeat(20);

        //then
        Assertions.assertThatThrownBy(() -> new RejectReason(rejectString))
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessage(null);
    }

    @DisplayName("거부이유는 null 일 수 없습니다.")
    @Test
    public void rejectReasonCannotBeNullTest() {

        Assertions.assertThatThrownBy(() -> new RejectReason(null))
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessage(null);
    }

    @DisplayName("거부이유는 빈 문자열일 수 없습니다.")
    @Test
    public void rejectReasonCannotBeBlankTest() {

        Assertions.assertThatThrownBy(() -> new RejectReason("\t"))
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessage(null);
    }
}
