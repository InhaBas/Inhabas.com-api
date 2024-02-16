package com.inhabas.api.domain.normalBoard.usecase;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.file.usecase.S3Service;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import com.inhabas.api.domain.normalBoard.domain.NormalBoard;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDetailDto;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDto;
import com.inhabas.api.domain.normalBoard.dto.SaveNormalBoardDto;
import com.inhabas.api.domain.normalBoard.repository.NormalBoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.inhabas.api.domain.menu.domain.MenuExampleTest.getNormalNoticeMenu;
import static com.inhabas.api.domain.menu.domain.valueObject.MenuGroupExampleTest.getNormalMenuGroup;
import static com.inhabas.api.domain.normalBoard.domain.NormalBoardType.NOTICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class NormalBoardServiceImplTest {

    @InjectMocks
    NormalBoardServiceImpl normalBoardService;

    @Mock
    MemberRepository memberRepository;
    @Mock
    NormalBoardRepository normalBoardRepository;
    @Mock
    MenuRepository menuRepository;
    @Mock
    S3Service s3Service;

    @DisplayName("normal board 게시글 목록을 조회한다.")
    @Test
    void getPosts() {
        // given
        Member member = MemberTest.chiefMember();
        NormalBoardDto dto = new NormalBoardDto(
                1L, "title", member.getName(), LocalDateTime.now(), LocalDateTime.now(), false);

        given(normalBoardRepository.findAllByTypeAndSearch(any(), any()))
                .willReturn(Arrays.asList(dto));

        // when
        List<NormalBoardDto> clubActivityDtoList = normalBoardService.getPosts(1L, NOTICE, "");

        // then
        assertThat(clubActivityDtoList).hasSize(1);
    }

    @DisplayName("normal board 게시글 단일 조회한다.")
    @Test
    void getPost() {
        // given
        Member member = MemberTest.chiefMember();
        Menu menu = getNormalNoticeMenu(getNormalMenuGroup());
        NormalBoard normalBoard =
                new NormalBoard("title", menu, "content", false)
                        .writtenBy(member, NormalBoard.class);

        given(normalBoardRepository.findByTypeAndId(any(), any())).willReturn(Optional.of(normalBoard));

        // when
        NormalBoardDetailDto dto = normalBoardService.getPost(1L, NOTICE, 1L);

        // then
        assertThat(dto.getTitle()).isEqualTo(normalBoard.getTitle());

    }

    @DisplayName("normal board 게시글을 작성한다.")
    @Test
    void write() {
        //given
        Member member = MemberTest.chiefMember();
        SaveNormalBoardDto saveNormalBoardDto = new SaveNormalBoardDto("title", "content", null, false);
        Menu menu = getNormalNoticeMenu(getNormalMenuGroup());
        NormalBoard normalBoard =
                new NormalBoard("title", menu, "content", false)
                        .writtenBy(member, NormalBoard.class);

        given(memberRepository.findById(any())).willReturn(Optional.of(member));
        given(normalBoardRepository.save(any())).willReturn(normalBoard);
        given(menuRepository.findById(anyInt())).willReturn(Optional.of(menu));

        // when
        normalBoardService.write(1L, NOTICE, saveNormalBoardDto);

        // then
        then(menuRepository).should(times(1)).findById(anyInt());
        then(normalBoardRepository).should(times(1)).save(any());
    }

    @DisplayName("normal board 게시글을 수정한다.")
    @Test
    void update() {
        //given
        SaveNormalBoardDto saveNormalBoardDto = new SaveNormalBoardDto("title", "content", null, false);
        Menu menu = getNormalNoticeMenu(getNormalMenuGroup());
        NormalBoard normalBoard = new NormalBoard("title", menu, "content", false);
        ReflectionTestUtils.setField(normalBoard, "id", 1L);

        given(normalBoardRepository.findById(any())).willReturn(Optional.of(normalBoard));
        given(normalBoardRepository.save(any())).willReturn(normalBoard);

        // when
        normalBoardService.update(normalBoard.getId(), NOTICE, saveNormalBoardDto);

        // then
        then(normalBoardRepository).should(times(1)).findById(any());
        then(normalBoardRepository).should(times(1)).save(any());
    }

    @DisplayName("normal board 게시글을 삭제한다.")
    @Test
    void delete() {
        // given
        Long boardId = 1L;
        doNothing().when(normalBoardRepository).deleteById(boardId);

        // when
        normalBoardService.delete(boardId);

        // then
        then(normalBoardRepository).should(times(1)).deleteById(boardId);
    }
}