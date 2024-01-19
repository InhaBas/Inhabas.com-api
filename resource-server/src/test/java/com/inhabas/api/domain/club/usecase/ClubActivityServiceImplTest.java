package com.inhabas.api.domain.club.usecase;

import com.inhabas.api.auth.domain.error.authException.InvalidAuthorityException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.board.domain.AlbumBoard;
import com.inhabas.api.domain.board.usecase.BoardSecurityChecker;
import com.inhabas.api.domain.club.dto.ClubActivityDetailDto;
import com.inhabas.api.domain.club.dto.ClubActivityDto;
import com.inhabas.api.domain.club.dto.SaveClubActivityDto;
import com.inhabas.api.domain.club.repository.ClubActivityRepository;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClubActivityServiceImplTest {

    @InjectMocks
    private ClubActivityServiceImpl clubActivityService;
    @Mock
    private ClubActivityRepository clubActivityRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BoardSecurityChecker boardSecurityChecker;
    @Mock
    private MenuRepository menuRepository;


    @DisplayName("동아리 활동 조회 성공")
    @Test
    public void getClubActivitiesTest_Success() {
        //given
        Member member = MemberTest.chiefMember();
        AlbumBoard clubActivity = AlbumBoard.builder()
                .writer(member)
                .title("title")
                .content("content")
                .menu(mock(Menu.class))
                .build();

        given(clubActivityRepository.findAll()).willReturn(Arrays.asList(clubActivity));

        //when
        List<ClubActivityDto> clubActivityDtoList = clubActivityService.getClubActivities();

        //then
        Assertions.assertThat(clubActivityDtoList.size()).isEqualTo(1);
        Assertions.assertThat(clubActivityDtoList.get(0).getTitle()).isEqualTo(clubActivity.getTitle());

    }

    @DisplayName("동아리 활동 생성 성공")
    @Test
    public void writeClubActivityTest_Success() {
        //given
        Long memberId = 1L;
        Member member = MemberTest.chiefMember(); // 필요한 속성으로 Member 객체 초기화
        SaveClubActivityDto saveClubActivityDto = new SaveClubActivityDto(
                "title", "content", null);

        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(clubActivityRepository.save(any(AlbumBoard.class))).willAnswer(invocation -> {
            AlbumBoard savedClubActivity = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedClubActivity, "id", 1L);
            return savedClubActivity;
        });

        // when
        Long resultId = clubActivityService.writeClubActivity(memberId, saveClubActivityDto);

        // then
        Assertions.assertThat(resultId).isEqualTo(1L);

    }

    @DisplayName("동아리 활동 단일 조회 성공")
    @Test
    public void getClubActivityTest_Success() {
        //given
        AlbumBoard clubActivity = AlbumBoard.builder()
                .writer(MemberTest.chiefMember())
                .title("title")
                .content("content")
                .menu(mock(Menu.class))
                .build();
        given(clubActivityRepository.findById(any())).willReturn(Optional.of(clubActivity));

        //when
        ClubActivityDetailDto clubActivityDetailDto = clubActivityService.getClubActivity(1L);

        //then
        assertThat(clubActivityDetailDto).as("Title and content of clubActivityDetailDto are equal to clubActivity")
                .extracting("title", "content")
                .containsExactly(clubActivity.getTitle(), clubActivity.getContent());


    }

    @DisplayName("동아리 활동 메뉴에 대한 읽기 권한이 없다면 단일 조회 실패")
    @Test
    public void getClubActivityTest_Forbidden() {
        //given
        AlbumBoard clubActivity = AlbumBoard.builder()
                .writer(MemberTest.chiefMember())
                .title("title")
                .content("content")
                .menu(mock(Menu.class))
                .build();
        given(clubActivityRepository.findById(any())).willReturn(Optional.of(clubActivity));
        doThrow(InvalidAuthorityException.class).when(boardSecurityChecker).checkMenuAccess(any(), any());

        //when
        ClubActivityDetailDto clubActivityDetailDto = clubActivityService.getClubActivity(1L);

        //then
        Assertions.assertThatThrownBy(() ->
                        boardSecurityChecker.checkMenuAccess(2, BoardSecurityChecker.READ_BOARD))
                .isInstanceOf(InvalidAuthorityException.class)
                .hasMessage("권한이 없습니다.");

    }

    @DisplayName("동아리 활동 수정 성공")
    @Test
    public void updateClubActivityTest_Success() {
        // given
        Member member = MemberTest.chiefMember();
        AlbumBoard clubActivity = AlbumBoard.builder()
                .writer(member)
                .title("title")
                .content("content")
                .menu(mock(Menu.class))
                .build();
        SaveClubActivityDto saveClubActivityDto = new SaveClubActivityDto(
                "title", "content", null);
        given(clubActivityRepository.findById(any())).willReturn(Optional.ofNullable(clubActivity));

        // when
        clubActivityService.updateClubActivity(1L, saveClubActivityDto);

        // then
        then(clubActivityRepository).should(times(1)).findById(any());

    }

    @DisplayName("동아리 활동 삭제 성공")
    @Test
    public void deleteClubActivityTest_Success() {
        //given
        Long boardId = 1L;
        doNothing().when(clubActivityRepository).deleteById(boardId);

        //when
        clubActivityService.deleteClubActivity(boardId);

        //then
        then(clubActivityRepository).should(times(1)).deleteById(boardId);

    }

}