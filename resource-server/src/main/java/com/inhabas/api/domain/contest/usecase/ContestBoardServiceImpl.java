package com.inhabas.api.domain.contest.usecase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.board.exception.S3UploadFailedException;
import com.inhabas.api.domain.board.usecase.BoardSecurityChecker;
import com.inhabas.api.domain.contest.domain.ContestBoard;
import com.inhabas.api.domain.contest.domain.valueObject.ContestType;
import com.inhabas.api.domain.contest.dto.ContestBoardDetailDto;
import com.inhabas.api.domain.contest.dto.ContestBoardDto;
import com.inhabas.api.domain.contest.dto.SaveContestBoardDto;
import com.inhabas.api.domain.contest.repository.ContestBoardRepository;
import com.inhabas.api.domain.contest.util.ClassifiedFiles;
import com.inhabas.api.domain.contest.util.ClassifyFiles;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.file.usecase.S3Service;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import com.inhabas.api.global.util.FileUtil;

@Service
@Transactional
@RequiredArgsConstructor
public class ContestBoardServiceImpl implements ContestBoardService {

  private final ContestBoardRepository contestBoardRepository;

  private final BoardSecurityChecker boardSecurityChecker;

  private final MemberRepository memberRepository;

  private final MenuRepository menuRepository;

  private final S3Service s3Service;

  private static final String CONTEST_BOARD_MENU_NAME = "공모전 게시판";

  private static final String DIR_NAME = "contest/";

  // 타입별 공모전 게시판 목록 조회
  @Override
  @Transactional(readOnly = true)
  public List<ContestBoardDto> getContestBoardsByType(
      ContestType contestType, Long contestFieldId, String search) {

    if (search == null || search.trim().isEmpty()) {
      search = "";
    }

    List<ContestBoard> contestBoardList =
        contestBoardRepository.findAllByContestTypeAndFieldLike(
            contestType, contestFieldId, search);

    return contestBoardList.stream()
        .map(
            contestBoard -> {
              ClassifiedFiles classifiedFiles =
                  ClassifyFiles.classifyFiles(new ArrayList<>(contestBoard.getFiles()));

              return ContestBoardDto.builder()
                  .id(contestBoard.getId())
                  .contestFieldId(contestBoard.getContestField().getId())
                  .title(contestBoard.getTitle())
                  .association(contestBoard.getAssociation())
                  .topic(contestBoard.getTopic())
                  .dateContestStart(contestBoard.getDateContestStart())
                  .dateContestEnd(contestBoard.getDateContestEnd())
                  .thumbnail(classifiedFiles.getThumbnail())
                  .build();
            })
        .collect(Collectors.toList());
  }

  // contestType 별로 게시글 작성
  @Override
  @Transactional
  public Long writeContestBoard(
      Long memberId, SaveContestBoardDto saveContestBoardDto, ContestType contestType) {

    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    Menu menu =
        menuRepository
            .findByName_Value(CONTEST_BOARD_MENU_NAME)
            .orElseThrow(NotFoundException::new);

    ContestBoard contestBoard =
        ContestBoard.builder()
            .title(saveContestBoardDto.getTitle())
            .content(saveContestBoardDto.getContent())
            .association(saveContestBoardDto.getAssociation())
            .topic(saveContestBoardDto.getTopic())
            .dateContestStart(saveContestBoardDto.getDateContestStart())
            .dateContestEnd(saveContestBoardDto.getDateContestEnd())
            .contestType(contestType)
            .contestFieldId(saveContestBoardDto.getContestFieldId())
            .build()
            .writtenBy(writer, ContestBoard.class);

    return updateContestBoardFiles(saveContestBoardDto, contestBoard);
  }

  // 공모전 게시판 단일조회
  @Override
  @Transactional(readOnly = true)
  public ContestBoardDetailDto getContestBoard(Long boardId) {

    ContestBoard contestBoard =
        contestBoardRepository.findById(boardId).orElseThrow(NotFoundException::new);

    ClassifiedFiles classifiedFiles =
        ClassifyFiles.classifyFiles(new ArrayList<>(contestBoard.getFiles()));

    return ContestBoardDetailDto.builder()
        .id(contestBoard.getId())
        .title(contestBoard.getTitle())
        .content(contestBoard.getContent())
        .writerName(contestBoard.getWriter().getName())
        .dateCreated(contestBoard.getDateCreated())
        .dateUpdated(contestBoard.getDateUpdated())
        .thumbnail(classifiedFiles.getThumbnail())
        .images(classifiedFiles.getImages())
        .otherFiles(classifiedFiles.getOtherFiles())
        .build();
  }

  @Override
  @Transactional
  public void updateContestBoard(Long boardId, SaveContestBoardDto saveContestBoardDto) {

    ContestBoard contestBoard =
        contestBoardRepository.findById(boardId).orElseThrow(NotFoundException::new);
    updateContestBoardFiles(saveContestBoardDto, contestBoard);
  }

  @Override
  @Transactional
  public void deleteContestBoard(Long boardId) {

    contestBoardRepository.deleteById(boardId);
  }

  private Long updateContestBoardFiles(
      SaveContestBoardDto saveContestBoardDto, ContestBoard contestBoard) {
    List<BoardFile> updateFiles = new ArrayList<>();
    List<String> urlListForDelete = new ArrayList<>();

    if (saveContestBoardDto.getFiles() != null) {
      contestBoard.updateContest(
          saveContestBoardDto.getContestFieldId(),
          saveContestBoardDto.getTitle(),
          saveContestBoardDto.getContent(),
          saveContestBoardDto.getAssociation(),
          saveContestBoardDto.getTopic(),
          saveContestBoardDto.getDateContestStart(),
          saveContestBoardDto.getDateContestEnd());
      try {
        updateFiles =
            saveContestBoardDto.getFiles().stream()
                .map(
                    file -> {
                      String path = FileUtil.generateFileName(file, DIR_NAME);
                      String url = s3Service.uploadS3File(file, path);
                      urlListForDelete.add(url);
                      return BoardFile.builder()
                          .name(file.getOriginalFilename())
                          .url(url)
                          .board(contestBoard)
                          .build();
                    })
                .collect(Collectors.toList());

      } catch (RuntimeException e) {
        for (String url : urlListForDelete) {
          s3Service.deleteS3File(url);
        }
        throw new S3UploadFailedException();
      }
    }

    contestBoard.updateFiles(updateFiles);
    return contestBoardRepository.save(contestBoard).getId();
  }
}
