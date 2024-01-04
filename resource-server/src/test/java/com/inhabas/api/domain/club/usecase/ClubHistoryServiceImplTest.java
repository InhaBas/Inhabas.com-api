package com.inhabas.api.domain.club.usecase;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.club.domain.entity.ClubHistory;
import com.inhabas.api.domain.club.dto.ClubHistoryDto;
import com.inhabas.api.domain.club.dto.SaveClubHistoryDto;
import com.inhabas.api.domain.club.repository.ClubHistoryRepository;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class ClubHistoryServiceImplTest {

    @InjectMocks
    ClubHistoryServiceImpl clubHistoryService;
    @Mock
    ClubHistoryRepository clubHistoryRepository;
    @Mock
    MemberRepository memberRepository;


    @DisplayName("동아리 연혁 생성 성공")
    @Test
    @Transactional
    void writeClubHistory_Success() {
        //given
        Long memberId = 1L;
        Member member = MemberTest.chiefMember(); // 필요한 속성으로 Member 객체 초기화
        SaveClubHistoryDto saveClubHistoryDto = new SaveClubHistoryDto(
                "title", "content", LocalDateTime.now());

        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(clubHistoryRepository.save(any(ClubHistory.class))).willAnswer(invocation -> {
            ClubHistory savedClubHistory = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedClubHistory, "id", 1L);
            return savedClubHistory;
        });

        // when
        Long resultId = clubHistoryService.writeClubHistory(memberId, saveClubHistoryDto);

        // then
        then(memberRepository).should().findById(memberId);
        then(clubHistoryRepository).should().save(any(ClubHistory.class));
        assertThat(resultId).isEqualTo(1L);
    }

    @DisplayName("동아리 연혁 생성 작성자가 존재하지 않을 시 Member nof found")
    @Test
    void writeClubHistory_Member_Not_Found() {
        //given
        SaveClubHistoryDto saveClubHistoryDto = new SaveClubHistoryDto(
                "title", "content", LocalDateTime.now());
        given(memberRepository.findById(any())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> clubHistoryService.writeClubHistory(1L, saveClubHistoryDto))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("존재 하지 않는 유저입니다.");

    }

    @DisplayName("동아리 연혁 단일 조회 성공")
    @Test
    void findClubHistory_Success() {
        //given
        ClubHistory clubHistory = ClubHistory.builder()
                .member(MemberTest.chiefMember())
                .title(new Title("title"))
                .content(new Content("content"))
                .dateHistory(LocalDateTime.now())
                .build();
        given(clubHistoryRepository.findById(any())).willReturn(Optional.of(clubHistory));

        //when
        ClubHistoryDto clubHistoryDto = clubHistoryService.findClubHistory(any());

        //then
        then(clubHistoryRepository).should().findById(any());
        assertThat(clubHistoryDto).as("clubHistoryDto's title and content are equal to clubHistory")
                .extracting("title", "content")
                .containsExactly(clubHistory.getTitle().getValue(), clubHistory.getContent().getValue());

    }

    @DisplayName("동아리 연혁 단일 조회 id가 존재하지 않으면 NOT_FOUND")
    @Test
    void findClubHistory_Not_Found() {
        //given
        given(clubHistoryRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> clubHistoryService.findClubHistory(any()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("데이터가 존재하지 않습니다.");

    }

    @DisplayName("동아리 연혁 조회 성공")
    @Test
    void getClubHistories_Success() {
        //given
        ClubHistory clubHistory = ClubHistory.builder()
                .member(MemberTest.chiefMember())
                .title(new Title("title"))
                .content(new Content("content"))
                .dateHistory(LocalDateTime.now())
                .build();
        List<ClubHistory> clubHistoryList = List.of(clubHistory);
        given(clubHistoryRepository.findAll()).willReturn(clubHistoryList);

        //when
        List<ClubHistoryDto> clubHistoryDtoList = clubHistoryService.getClubHistories();

        //then
        then(clubHistoryRepository).should().findAll();
        assertThat(clubHistoryDtoList)
                .hasSize(1)
                .extracting("title", "content")
                .contains(tuple(clubHistory.getTitle().getValue(), clubHistory.getContent().getValue()));

    }

    @DisplayName("동아리 연혁 수정 성공")
    @Test
    @Transactional
    void updateClubHistory_Success() {
        // given
        Member member = MemberTest.chiefMember();
        ClubHistory clubHistory = ClubHistory.builder()
                .member(member)
                .title(new Title("title"))
                .content(new Content("content"))
                .dateHistory(LocalDateTime.now())
                .build();
        SaveClubHistoryDto saveClubHistoryDto = new SaveClubHistoryDto(
                "title", "content", LocalDateTime.now());
        given(clubHistoryRepository.findById(any())).willReturn(Optional.ofNullable(clubHistory));
        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        // when
        clubHistoryService.updateClubHistory(1L, 1L, saveClubHistoryDto);

        // then
        then(clubHistoryRepository).should().findById(any());
        then(memberRepository).should().findById(any());

    }

    @DisplayName("동아리 연혁 삭제 성공")
    @Test
    @Transactional
    void deleteClubHistories_Success() {
        //given
        ClubHistory clubHistory = ClubHistory.builder()
                .member(MemberTest.chiefMember())
                .title(new Title("title"))
                .content(new Content("content"))
                .dateHistory(LocalDateTime.now())
                .build();
        given(clubHistoryRepository.findById(any())).willReturn(Optional.ofNullable(clubHistory));

        //when
        clubHistoryService.deleteClubHistory(any());

        //then
        then(clubHistoryRepository).should().findById(any());
        then(clubHistoryRepository).should().delete(any());

    }

}