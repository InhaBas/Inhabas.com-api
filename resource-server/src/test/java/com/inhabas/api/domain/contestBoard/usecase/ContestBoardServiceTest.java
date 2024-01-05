package com.inhabas.api.domain.contestBoard.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.board.BoardCannotModifiableException;
import com.inhabas.api.domain.contest.domain.ContestBoard;
import com.inhabas.api.domain.contest.dto.DetailContestBoardDto;
import com.inhabas.api.domain.contest.dto.ListContestBoardDto;
import com.inhabas.api.domain.contest.dto.SaveContestBoardDto;
import com.inhabas.api.domain.contest.dto.UpdateContestBoardDto;
import com.inhabas.api.domain.contest.repository.ContestBoardRepository;
import com.inhabas.api.domain.contest.usecase.ContestBoardServiceImpl;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class ContestBoardServiceTest {

    @InjectMocks
    ContestBoardServiceImpl contestBoardService;

    @Mock
    ContestBoardRepository contestBoardRepository;


    @DisplayName("공모전 게시글을 성공적으로 생성한다.")
    @Test
    public void createContestBoard() {
        //given
        StudentId StudentId = new StudentId("12201863");
        SaveContestBoardDto saveContestBoardDto =
                new SaveContestBoardDto("title", "content", "association", "topic",
                        LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 26));
        ContestBoard contestBoard =
                new ContestBoard("title", "content", "association", "topic",
                        LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 26));

        given(contestBoardRepository.save(any())).willReturn(contestBoard);

        // when
        Integer returnedId = contestBoardService.write(StudentId, saveContestBoardDto);

        // then
        then(contestBoardRepository).should(times(1)).save(any());
    }

    @DisplayName("공모전 게시판의 목록을 조회한다.")
    @Test
    public void getContestBoardList() {
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "deadline");

        ListContestBoardDto contestBoardDto1 =
                new ListContestBoardDto("title1", "contents1", LocalDate.of(2022, 1, 1),
                        LocalDate.of(2022, 3, 4));
        ListContestBoardDto contestBoardDto2 =
                new ListContestBoardDto("title2", "contents2", LocalDate.of(2022, 1, 1),
                        LocalDate.of(2022, 5, 28));
        ListContestBoardDto contestBoardDto3 =
                new ListContestBoardDto("title2", "contents2", LocalDate.of(2022, 1, 1),
                        LocalDate.of(2022, 2, 20));

        List<ListContestBoardDto> results = new ArrayList<>();
        results.add(contestBoardDto3);
        results.add(contestBoardDto1);
        results.add(contestBoardDto2);

        Page<ListContestBoardDto> expectedContestBoardDto = new PageImpl<>(results, pageable,
                results.size());

        given(contestBoardRepository.findAllByMenuId(any(), any())).willReturn(
                expectedContestBoardDto);

        //when
        Page<ListContestBoardDto> returnedBoardList = contestBoardService.getBoardList(
                new MenuId(1), pageable);

        //then
        assertThat(returnedBoardList).isEqualTo(expectedContestBoardDto);

    }

    @DisplayName("공모전 게시글 단일 조회에 성공한다.")
    @Test
    public void getDetailBoard() {
        //given
        DetailContestBoardDto contestBoardDto =
                new DetailContestBoardDto(1, "mingyeom", "title", "content",
                        "association", "topic", LocalDate.of(2022, 1, 1),
                        LocalDate.of(2022, 1, 29), LocalDateTime.now(), null);
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
        StudentId StudentId = new StudentId("12201863");
        ContestBoard contestBoard =
                new ContestBoard("title", "content", "association", "topic",
                        LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 26))
                        .writtenBy(StudentId);
        given(contestBoardRepository.findById(anyInt())).willReturn(Optional.of(contestBoard));
        doNothing().when(contestBoardRepository).deleteById(any());

        // when
        contestBoardService.delete(StudentId, 1);

        // then
        then(contestBoardRepository).should(times(1)).deleteById(any());
    }

    @DisplayName("공모전 게시글을 수정한다.")
    @Test
    public void updateContestBoard() {
        //given
        StudentId StudentId = new StudentId("12201863");
        ContestBoard expectedContestBoard =
                new ContestBoard("title", "content", "association", "topic",
                        LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 26))
                        .writtenBy(StudentId);

        given(contestBoardRepository.save(any())).willReturn(expectedContestBoard);
        given(contestBoardRepository.findById(any())).willReturn(Optional.of(expectedContestBoard));

        UpdateContestBoardDto updateContestBoardDto = new UpdateContestBoardDto(1, "수정된 제목",
                "수정된 내용", "수정된 협회기관명", "수정된 공모전 주제", LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 26));

        // when
        contestBoardService.update(StudentId, updateContestBoardDto);

        // then
        then(contestBoardRepository).should(times(1)).save(any());
    }

    @DisplayName("작성자가 아니면 수정할 수 없다.")
    @Test
    public void failToModifyTest() {
        //given
        StudentId badUser = new StudentId("44444444");
        StudentId originalWriter = new StudentId("12201863");
        ContestBoard expectedContestBoard =
                new ContestBoard("title", "content", "association", "topic",
                        LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 26))
                        .writtenBy(originalWriter);
        given(contestBoardRepository.findById(any())).willReturn(Optional.of(expectedContestBoard));

        UpdateContestBoardDto updateContestBoardDto = new UpdateContestBoardDto(1, "수정된 제목",
                "수정된 내용", "수정된 협회기관명", "수정된 공모전 주제", LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 26));

        // when
        Assertions.assertThrows(BoardCannotModifiableException.class,
                ()->contestBoardService.update(badUser, updateContestBoardDto));

        //then
        then(contestBoardRepository).should(times(0)).save(any());
    }

    @DisplayName("작성자가 아니면 삭제할 수 없다.")
    @Test
    public void failToDeleteTest() {
        //given
        StudentId badUser = new StudentId("44444444");
        StudentId StudentId = new StudentId("12201863");
        ContestBoard contestBoard =
                new ContestBoard("title", "content", "association", "topic",
                        LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 26))
                        .writtenBy(StudentId);
        given(contestBoardRepository.findById(anyInt())).willReturn(Optional.of(contestBoard));

        // when
        Assertions.assertThrows(BoardCannotModifiableException.class,
                ()->contestBoardService.delete(badUser, 1));

        // then
        then(contestBoardRepository).should(times(0)).deleteById(any());
    }
}
