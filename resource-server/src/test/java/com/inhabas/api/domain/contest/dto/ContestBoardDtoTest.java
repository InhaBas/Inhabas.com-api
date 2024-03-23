package com.inhabas.api.domain.contest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import com.inhabas.api.domain.file.dto.FileDownloadDto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ContestBoardDtoTest {

  @DisplayName("올바른 공모전을 생성하고 검증한다.")
  @Test
  public void createContestBoardDto() {

    // given
    Long id = 1L;
    Long contestFieldId = 1L;
    String title = "테스트 제목";
    String topic = "테스트 주제";
    String association = "테스트 협회";
    LocalDate dateContestStart = LocalDate.of(2024, 1, 1);
    LocalDate dateContestEnd = LocalDate.of(2024, 3, 1);
    FileDownloadDto thumbnail =
        new FileDownloadDto("random", "thumbnail.jpg", "/thumbnailUrl", 10L, "image/jpeg");

    // when
    ContestBoardDto contestBoardDto =
        ContestBoardDto.builder()
            .id(id)
            .contestFieldId(contestFieldId)
            .title(title)
            .topic(topic)
            .association(association)
            .dateContestStart(dateContestStart)
            .dateContestEnd(dateContestEnd)
            .thumbnail(thumbnail)
            .build();

    // then
    assertThat(contestBoardDto.getId()).isEqualTo(id);
    assertThat(contestBoardDto.getContestFieldId()).isEqualTo(contestFieldId);
    assertThat(contestBoardDto.getTitle()).isEqualTo(title);
    assertThat(contestBoardDto.getTopic()).isEqualTo(topic);
    assertThat(contestBoardDto.getAssociation()).isEqualTo(association);
    assertThat(contestBoardDto.getDateContestStart()).isEqualTo(dateContestStart);
    assertThat(contestBoardDto.getDateContestEnd()).isEqualTo(dateContestEnd);
    assertThat(contestBoardDto.getThumbnail()).isEqualTo(thumbnail);

    long expectedDDay = LocalDate.now().until(dateContestEnd, java.time.temporal.ChronoUnit.DAYS);
    assertThat(contestBoardDto.getDDay()).as("D-day가 일치하지 않습니다.").isEqualTo(expectedDDay);
  }
}
