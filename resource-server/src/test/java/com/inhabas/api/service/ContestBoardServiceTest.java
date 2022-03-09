package com.inhabas.api.service;

import com.inhabas.api.domain.contest.ContestBoard;
import com.inhabas.api.domain.contest.ContestBoardRepository;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.MemberRepository;
import com.inhabas.api.dto.contest.DetailContestBoardDto;
import com.inhabas.api.dto.contest.ListContestBoardDto;
import com.inhabas.api.dto.contest.SaveContestBoardDto;
import com.inhabas.api.dto.contest.UpdateContestBoardDto;
import com.inhabas.api.service.contest.ContestBoardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ContestBoardServiceTest {

    @InjectMocks
    ContestBoardServiceImpl contestBoardService;

    @Mock
    ContestBoardRepository contestBoardRepository;

    @Mock
    MemberRepository memberRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(contestBoardService).build();
    }

    @DisplayName("공모전 게시글을 성공적으로 생성한다.")
    @Test
    public void createContestBoard() {
        //given
        SaveContestBoardDto saveContestBoardDto = new SaveContestBoardDto("title", "contents", "association", "topic", LocalDate.of(2022, 01, 01), LocalDate.of(2022, 01,26) );
        ContestBoard contestBoard = new ContestBoard(1, "title", "contents", "association", "topic", LocalDate.of(2022, 01, 01), LocalDate.of(2022, 01,26) );
        Member writer = new Member(12201863, "mingyeom", "010-0000-0000","picture", null, null);

        given(contestBoardRepository.save(any())).willReturn(contestBoard);
        given(memberRepository.getById(anyInt())).willReturn(writer);

        // when
        Integer returnedId = contestBoardService.write(12201863, saveContestBoardDto);

        // then
        then(contestBoardRepository).should(times(1)).save(any());
        assertThat(returnedId).isNotNull();
        assertThat(returnedId).isEqualTo(contestBoard.getId());
    }

    @DisplayName("공모전 게시판의 목록을 조회한다.")
    @Test
    public void getContestBoardList() {
        //given
        Pageable pageable = PageRequest.of(0,10, Sort.Direction.DESC, "deadline");

        ListContestBoardDto contestBoardDto1 = new ListContestBoardDto("title1", "contents1", LocalDate.of(2022,01,01), LocalDate.of(2022, 03, 4));
        ListContestBoardDto contestBoardDto2 = new ListContestBoardDto("title2", "contents2", LocalDate.of(2022,01,01), LocalDate.of(2022, 05, 28));
        ListContestBoardDto contestBoardDto3 = new ListContestBoardDto("title2", "contents2", LocalDate.of(2022,01,01), LocalDate.of(2022, 02, 20));

        List<ListContestBoardDto> results = new ArrayList<>();
        results.add(contestBoardDto3);
        results.add(contestBoardDto1);
        results.add(contestBoardDto2);


        Page<ListContestBoardDto> expectedContestBoardDto = new PageImpl<>(results, pageable, results.size());

        given(contestBoardRepository.findAllByMenuId(any(), any())).willReturn(expectedContestBoardDto);

        //when
        Page<ListContestBoardDto> returnedBoardList = contestBoardService.getBoardList(1, pageable);

        //then
        assertThat(returnedBoardList).isEqualTo(expectedContestBoardDto);

    }

    @DisplayName("공모전 게시글 단일 조회에 성공한다.")
    @Test
    public void getDetailBoard() {
        //given
        DetailContestBoardDto contestBoardDto = new DetailContestBoardDto(1, "mingyeom", "title", "contents", "association","topic",  LocalDate.of(2022,01,01), LocalDate.of(2022, 01, 29), LocalDateTime.now(), null);
        given(contestBoardRepository.findDtoById(any())).willReturn(Optional.of(contestBoardDto));

        // when
        contestBoardService.getBoard(1);

        // then
        then(contestBoardRepository).should(times(1)).findDtoById(any());
    }


    @DisplayName("공모전 게시글을 성공적으로 삭제한다.")
    @Test
    public void deleteContestBoard() {
        //given
        doNothing().when(contestBoardRepository).deleteById(any());

        // when
        contestBoardService.delete(1);

        // then
        then(contestBoardRepository).should(times(1)).deleteById(any());
    }

    @DisplayName("공모전 게시글을 수정한다.")
    @Test
    public void updateContestBoard() {
        //given
        Member writer = new Member(12201863, "mingyeom", "010-0000-0000","picture", null, null);
        ContestBoard expectedContestBoard = new ContestBoard(1, "title", "contents", "association", "topic", LocalDate.of(2022, 01, 01), LocalDate.of(2022, 01,26) )
                .writtenBy(writer);

        given(contestBoardRepository.save(any())).willReturn(expectedContestBoard);
        given(contestBoardRepository.findById(any())).willReturn(Optional.of(expectedContestBoard));

        UpdateContestBoardDto updateContestBoardDto = new UpdateContestBoardDto(1, "수정된 제목", "수정된 내용", "수정된 협회기관명", "수정된 공모전 주제",LocalDate.of(2022, 01, 01), LocalDate.of(2022, 01,26) );

        // when
        Integer returnedId = contestBoardService.update(12201863, updateContestBoardDto);

        // then
        then(contestBoardRepository).should(times(1)).save(any());
        assertThat(returnedId).isNotNull();
        assertThat(returnedId).isEqualTo(expectedContestBoard.getId());
    }
}
