package com.inhabas.api.domain.club.domain.entity;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.club.domain.ClubHistory;
import com.inhabas.api.domain.club.dto.SaveClubHistoryDto;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


class ClubHistoryTest {

    @Test
    void updateClubHistory() {
        //given
        Member writer = MemberTest.chiefMember();
        ClubHistory clubHistory = ClubHistory.builder()
                .member(writer)
                .title(new Title("oldTitle"))
                .content(new Content("oldContent"))
                .dateHistory(LocalDateTime.now())
                .build();
        SaveClubHistoryDto saveClubHistoryDto = SaveClubHistoryDto.builder()
                .title("title")
                .content("content")
                .dateHistory(LocalDateTime.now())
                .build();

        //when
        clubHistory.updateClubHistory(writer, saveClubHistoryDto);

        //then
        assertThat(clubHistory)
                .extracting(ClubHistory -> clubHistory.getTitle().getValue(),
                        ClubHistory -> clubHistory.getContent().getValue())
                .containsExactly(saveClubHistoryDto.getTitle(), saveClubHistoryDto.getContent());

    }

}