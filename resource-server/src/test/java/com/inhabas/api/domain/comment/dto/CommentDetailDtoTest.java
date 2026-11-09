package com.inhabas.api.domain.comment.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;

import org.junit.jupiter.api.Test;

class CommentDetailDtoTest {

  @Test
  void withdrawnWriterWithoutSchoolInformationDoesNotThrow() {
    Member writer = new Member("회원", "member@example.com", Role.BASIC);
    writer.withdraw();

    CommentDetailDto dto = new CommentDetailDto(1L, writer, "내용", false, LocalDateTime.now());

    assertThat(dto.getWriterInfo().getName()).isEqualTo("탈퇴한 회원");
    assertThat(dto.getWriterInfo().getMajor()).isNull();
  }
}
