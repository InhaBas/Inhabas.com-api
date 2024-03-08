package com.inhabas.api.domain.contest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.inhabas.api.domain.file.dto.FileDownloadDto;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContestBoardDetailDtoTest {

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

  @DisplayName("ContestBoardDetailDto 객체를 정상적으로 생성한다.")
  @Test
  public void createContestBoardDetailDto() {
    // given
    ContestBoardDetailDto contestBoardDetailDto =
        ContestBoardDetailDto.builder()
            .id(1L)
            .contestFieldId(1L)
            .title("테스트 제목")
            .content("테스트 내용")
            .writerName("송민석")
            .association("(주) 아이바스")
            .topic("테스트 주제")
            .dateContestStart(LocalDate.now())
            .dateContestEnd(LocalDate.now().plusDays(10))
            .dateCreated(LocalDateTime.now())
            .dateUpdated(LocalDateTime.now())
            .thumbnail(new FileDownloadDto("thumbnail.jpg", "thumbnailUrl"))
            .images(new ArrayList<>())
            .otherFiles(new ArrayList<>())
            .build();

    // when
    Set<ConstraintViolation<ContestBoardDetailDto>> violations =
        validator.validate(contestBoardDetailDto);

    // then
    assertThat(violations).isEmpty();
  }

  @DisplayName("ContestBoardDetailDto title 필드가 null 이면 validation 실패")
  @Test
  public void nullTitleTest() {
    // given
    ContestBoardDetailDto contestBoardDetailDto =
        ContestBoardDetailDto.builder()
            .id(1L)
            .contestFieldId(1L)
            // title 필드 null
            .content("테스트 내용")
            .writerName("송민석")
            .association("(주) 아이바스")
            .topic("테스트 주제")
            .dateContestStart(LocalDate.now())
            .dateContestEnd(LocalDate.now().plusDays(10))
            .dateCreated(LocalDateTime.now())
            .dateUpdated(LocalDateTime.now())
            .thumbnail(new FileDownloadDto("thumbnail.jpg", "thumbnailUrl"))
            .images(new ArrayList<>())
            .otherFiles(new ArrayList<>())
            .build();

    // when
    Set<ConstraintViolation<ContestBoardDetailDto>> violations =
        validator.validate(contestBoardDetailDto);

    // then
    assertThat(violations).hasSize(1);
  }
}
