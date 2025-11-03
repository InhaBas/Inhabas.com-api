package com.inhabas.api.domain.myInfo.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MyBudgetSupportApplicationsTest {

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

  @DisplayName("MyBudgetSupportApplicationsDto 객체를 정상적으로 생성한다.")
  @Test
  public void MyBoardsDto_is_OK() {
    // given
    MyBudgetSupportApplicationDto myBudgetSupportApplicationDto =
        MyBudgetSupportApplicationDto.builder()
            .id(1L)
            .status(RequestStatus.COMPLETED)
            .title("예산신청 제목")
            .dateCreated(LocalDateTime.now())
            .dateChecked(LocalDateTime.now())
            .dateDeposited(LocalDateTime.now())
            .build();

    // when
    Set<ConstraintViolation<MyBudgetSupportApplicationDto>> violations =
        validator.validate(myBudgetSupportApplicationDto);

    // then
    assertThat(violations).isEmpty();
  }

  @DisplayName("MyBudgetSupportApplicationsDto status 필드가 null 이면 validation 실패")
  @Test
  public void MenuName_is_null() {
    // given
    MyBudgetSupportApplicationDto myBudgetSupportApplicationDto =
        MyBudgetSupportApplicationDto.builder()
            .id(1L)
            .status(null)
            .title("예산신청 제목")
            .dateCreated(LocalDateTime.now())
            .dateChecked(LocalDateTime.now())
            .dateDeposited(LocalDateTime.now())
            .build();

    // when
    Set<ConstraintViolation<MyBudgetSupportApplicationDto>> violations =
        validator.validate(myBudgetSupportApplicationDto);

    // then
    assertThat(violations).hasSize(1);
  }
}
