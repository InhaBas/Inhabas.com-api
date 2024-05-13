package com.inhabas.api.domain.contest.usecase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.contest.domain.ContestBoard;
import com.inhabas.api.domain.contest.domain.ContestField;
import com.inhabas.api.domain.contest.domain.ContestType;
import com.inhabas.api.domain.contest.domain.valueObject.OrderBy;
import com.inhabas.api.domain.contest.dto.ContestBoardDetailDto;
import com.inhabas.api.domain.contest.dto.ContestBoardDto;
import com.inhabas.api.domain.contest.dto.SaveContestBoardDto;
import com.inhabas.api.domain.contest.repository.ContestBoardRepository;
import com.inhabas.api.domain.contest.repository.ContestFieldRepository;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.file.repository.BoardFileRepository;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import com.inhabas.api.global.util.ClassifiedFiles;
import com.inhabas.api.global.util.ClassifyFiles;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContestBoardServiceImpl implements ContestBoardService {

  private final ContestBoardRepository contestBoardRepository;
  private final ContestFieldRepository contestFieldRepository;
  private final MemberRepository memberRepository;
  private final MenuRepository menuRepository;
  private final BoardFileRepository boardFileRepository;

  // 타입별 공모전 게시판 목록 조회
  @Override
  @Transactional(readOnly = true)
  public List<ContestBoardDto> getContestBoards(
      ContestType contestType, Long contestFieldId, String search, OrderBy orderBy) {

    if (search == null || search.trim().isEmpty()) {
      search = "";
    }

    return contestBoardRepository.findAllByTypeAndFieldAndSearch(
        contestType, contestFieldId, search, orderBy);
  }

  // 공모전 게시판 단일조회
  @Override
  @Transactional(readOnly = true)
  public ContestBoardDetailDto getContestBoard(ContestType contestType, Long boardId) {
    ContestBoard contestBoard =
        contestBoardRepository
            .findByTypeAndId(contestType, boardId)
            .orElseThrow(NotFoundException::new);

    List<BoardFile> files =
        Optional.ofNullable(contestBoard.getFiles()).orElse(Collections.emptyList());
    ClassifiedFiles classifiedFiles = ClassifyFiles.classifyFiles(new ArrayList<>(files));

    return ContestBoardDetailDto.builder()
        .id(contestBoard.getId())
        .contestFieldId(contestBoard.getContestField().getId())
        .title(contestBoard.getTitle())
        .content(contestBoard.getContent())
        .writerName(contestBoard.getWriter().getName())
        .association(contestBoard.getAssociation())
        .topic(contestBoard.getTopic())
        .dateContestStart(contestBoard.getDateContestStart())
        .dateContestEnd(contestBoard.getDateContestEnd())
        .dateCreated(contestBoard.getDateCreated())
        .dateUpdated(contestBoard.getDateUpdated())
        .thumbnail(classifiedFiles.getThumbnail())
        .images(classifiedFiles.getImages())
        .otherFiles(classifiedFiles.getOtherFiles())
        .build();
  }

  // contestType 별로 게시글 작성
  @Override
  @Transactional
  public Long writeContestBoard(
      Long memberId, ContestType contestType, SaveContestBoardDto saveContestBoardDto) {

    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    Menu menu =
        menuRepository.findById(contestType.getMenuId()).orElseThrow(NotFoundException::new);

    ContestField contestField =
        contestFieldRepository
            .findById(saveContestBoardDto.getContestFieldId())
            .orElseThrow(NotFoundException::new);
    ContestBoard contestBoard =
        ContestBoard.builder()
            .menu(menu)
            .contestField(contestField)
            .title(saveContestBoardDto.getTitle())
            .content(saveContestBoardDto.getContent())
            .association(saveContestBoardDto.getAssociation())
            .topic(saveContestBoardDto.getTopic())
            .dateContestStart(saveContestBoardDto.getDateContestStart())
            .dateContestEnd(saveContestBoardDto.getDateContestEnd())
            .build()
            .writtenBy(writer, ContestBoard.class);

    List<String> fileIdList = saveContestBoardDto.getFiles();
    List<BoardFile> boardFileList = boardFileRepository.getAllByIdInAndUploader(fileIdList, writer);
    contestBoard.updateFiles(boardFileList);

    for (BoardFile file : boardFileList) {
      file.toBoard(contestBoard);
    }
    return contestBoardRepository.save(contestBoard).getId();
  }

  @Override
  @Transactional
  public void updateContestBoard(
      Long boardId,
      ContestType contestType,
      SaveContestBoardDto saveContestBoardDto,
      Long memberId) {
    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    ContestBoard contestBoard =
        contestBoardRepository.findById(boardId).orElseThrow(NotFoundException::new);
    ContestField contestField =
        contestFieldRepository
            .findById(saveContestBoardDto.getContestFieldId())
            .orElseThrow(NotFoundException::new);
    contestBoard.updateContest(
        contestField,
        saveContestBoardDto.getTitle(),
        saveContestBoardDto.getContent(),
        saveContestBoardDto.getAssociation(),
        saveContestBoardDto.getTopic(),
        saveContestBoardDto.getDateContestStart(),
        saveContestBoardDto.getDateContestEnd());

    List<String> fileIdList = saveContestBoardDto.getFiles();
    List<BoardFile> boardFileList = boardFileRepository.getAllByIdInAndUploader(fileIdList, writer);
    contestBoard.updateFiles(boardFileList);

    for (BoardFile file : boardFileList) {
      file.toBoard(contestBoard);
    }
  }

  @Override
  @Transactional
  public void deleteContestBoard(Long boardId) {

    contestBoardRepository.deleteById(boardId);
  }
}
