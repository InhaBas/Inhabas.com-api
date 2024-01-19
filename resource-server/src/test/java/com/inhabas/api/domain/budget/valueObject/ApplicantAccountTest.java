package com.inhabas.api.domain.budget.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.inhabas.api.domain.budget.domain.valueObject.ApplicantAccount;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ApplicantAccountTest {

    @DisplayName("Account 타입에 문자열을 저장한다.")
    @Test
    public void normalApplicantAccountTest() {
        //given
        String  accountString = "계좌 정보입니다.";

        //when
        ApplicantAccount applicantAccount = new ApplicantAccount(accountString);

        //then
        assertThat(applicantAccount.getValue()).isEqualTo("계좌 정보입니다.");
    }

    @DisplayName("Account 타입에 너무 긴 문자열을 저장한다. 100자 이상")
    @Test
    public void tooLongApplicantAccountTest() {
        //given
        String accountString = "지금이문장은10자임".repeat(10);

        //then
        assertThatThrownBy(() -> new ApplicantAccount(accountString))
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessage(null);
    }

    @DisplayName("계좌정보는 null 일 수 없습니다.")
    @Test
    public void applicantAccountCannotBeNull() {
        assertThatThrownBy(() -> new ApplicantAccount(null))
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessage(null);
    }

    @DisplayName("계좌정보는 빈 문자열일 수 없습니다.")
    @Test
    public void applicantAccountCannotBeNullBlank() {
        assertThatThrownBy(() -> new ApplicantAccount("\t"))
               .isInstanceOf(IllegalArgumentException.class)
               .hasMessage(null);
    }
}
