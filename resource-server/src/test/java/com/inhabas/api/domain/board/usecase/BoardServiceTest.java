package com.inhabas.api.domain.board.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyInt;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;

import com.inhabas.api.domain.board.domain.NormalBoard;
import com.inhabas.api.domain.board.repository.NormalBoardRepository;
import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.board.dto.BoardDto;
import com.inhabas.api.domain.board.dto.SaveBoardDto;
import com.inhabas.api.domain.board.dto.UpdateBoardDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @InjectMocks
    BoardServiceImpl boardService;

    @Mock
    NormalBoardRepository boardRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(boardService).build();
    }

    @DisplayName("게시판을 성공적으로 생성한다.")
    @Test
    public void createBoard() {
        //given
        SaveBoardDto saveBoardDto = new SaveBoardDto("title", "contents", new MenuId(1));
        NormalBoard normalBoard = new NormalBoard(1, "title", "contents");
        Menu menu = new Menu(null, 1, null, "name", "description");
        Member member = new Member(new MemberId(12201863), "mingyeom", "010-0000-0000","my@gmail.com", "picture", null, null);
        given(boardRepository.save(any())).willReturn(normalBoard);

        // when
        Integer returnedId = boardService.write(new MemberId(12201863), saveBoardDto);

        // then
        then(boardRepository).should(times(1)).save(any());
        assertThat(returnedId).isNotNull();
        assertThat(returnedId).isEqualTo(normalBoard.getId());
    }

    @DisplayName("게시판의 목록을 조회한다.")
    @Test
    public void getBoardList() {
        //given
        PageRequest pageable = PageRequest.of(0,10, Sort.Direction.ASC, "created");

        BoardDto boardDto1 =
                new BoardDto(1, "title", "contents", "mingyeom",
                        new MenuId(1), LocalDateTime.now(), LocalDateTime.now() );
        BoardDto boardDto2 =
                new BoardDto(2, "title", "contents", "minji",
                        new MenuId(1), LocalDateTime.now(), LocalDateTime.now() );

        List<BoardDto> results = new ArrayList<>();
        results.add(boardDto1);
        results.add(boardDto2);
        Page<BoardDto> expectedBoardDto = new PageImpl<> (results, pageable, results.size());

        given(boardRepository.findAllByMenuId(any(), any())).willReturn(expectedBoardDto);

        //when
        Page<BoardDto> returnedBoardList = boardService.getBoardList(new MenuId(1), pageable);

        //then
        then(boardRepository).should(times(1)).findAllByMenuId(any(), any());
        assertThat(returnedBoardList).isEqualTo(expectedBoardDto);
    }

    @DisplayName("게시글 단일 조회에 성공한다.")
    @Test
    public void getDetailBoard() {
        //given
        BoardDto boardDto =
                new BoardDto(1, "title", "contents", "김민겸",
                        new MenuId(1), LocalDateTime.now() , null);
        given(boardRepository.findDtoById(any())).willReturn(Optional.of(boardDto));

        // when
        boardService.getBoard(1);

        // then
        then(boardRepository).should(times(1)).findDtoById(any());
    }

    @DisplayName("게시글을 성공적으로 삭제한다.")
    @Test
    public void deleteBoard() {
        //given
        doNothing().when(boardRepository).deleteById(any());

        // when
        boardService.delete(1);

        // then
        then(boardRepository).should(times(1)).deleteById(any());
    }

    @DisplayName("게시글을 수정한다.")
    @Test
    public void updateBoard() {
        //given
        MemberId memberId = new MemberId(12201863);
        NormalBoard savedNormalBoard = new NormalBoard(1, "Origin Title", "Origin Contents").writtenBy(memberId);
        NormalBoard updatedNormalBoard = new NormalBoard(1, "Title", "Contents").writtenBy(memberId);

        given(boardRepository.findById(anyInt())).willReturn(Optional.of(savedNormalBoard));
        given(boardRepository.save(any())).willReturn(updatedNormalBoard);

        UpdateBoardDto updateBoardDto = new UpdateBoardDto(1, "수정된 제목", "수정된 내용");

        // when
        Integer returnedId = boardService.update(memberId, updateBoardDto);

        // then
        then(boardRepository).should(times(1)).save(any());
        assertThat(returnedId).isNotNull();
        assertThat(returnedId).isEqualTo(updatedNormalBoard.getId());
    }

    @Disabled
    @DisplayName("게시글을 생성한 유저와 일치하지 않아 게시글 수정에 실패한다.")
    @Test
    public void failToUpdateBoard() {
        //given

        // when

        // then
    }

}
