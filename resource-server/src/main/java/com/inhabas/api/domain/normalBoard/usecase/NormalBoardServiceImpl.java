package com.inhabas.api.domain.normalBoard.usecase;

import static com.inhabas.api.domain.board.domain.PinOption.*;
import static com.inhabas.api.domain.normalBoard.domain.NormalBoardType.EXECUTIVE;
import static com.inhabas.api.domain.normalBoard.domain.NormalBoardType.NOTICE;

import java.time.LocalDateTime;
import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.file.repository.BoardFileRepository;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import com.inhabas.api.domain.normalBoard.domain.NormalBoard;
import com.inhabas.api.domain.normalBoard.domain.NormalBoardType;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDetailDto;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDto;
import com.inhabas.api.domain.normalBoard.dto.SaveNormalBoardDto;
import com.inhabas.api.domain.normalBoard.repository.NormalBoardRepository;
import com.inhabas.api.global.util.ClassifiedFiles;
import com.inhabas.api.global.util.ClassifyFiles;

@Service
@Slf4j
@RequiredArgsConstructor
public class NormalBoardServiceImpl implements NormalBoardService {

  private final NormalBoardRepository normalBoardRepository;
  private final MenuRepository menuRepository;
  private final MemberRepository memberRepository;
  private final BoardFileRepository boardFileRepository;

  private static final Set<NormalBoardType> hasPinnedBoardTypeSet =
      new HashSet<>(Arrays.asList(NOTICE, EXECUTIVE));
  private static final LocalDateTime PERMANENT_DATE = LocalDateTime.of(2200, 1, 1, 0, 0, 0);
  private static final Integer TEMPORARY_DAYS = 14;

  @Override
  @Transactional(readOnly = true)
  public List<NormalBoardDto> getPinned(NormalBoardType boardType) {
    List<NormalBoardDto> normalBoardList = new ArrayList<>();
    if (boardType.equals(NOTICE) || boardType.equals(EXECUTIVE)) {
      normalBoardList = normalBoardRepository.findAllByTypeAndIsPinned(boardType);
    }
    return normalBoardList;
  }

  @Override
  @Transactional(readOnly = true)
  public List<NormalBoardDto> getPosts(NormalBoardType boardType, String search) {
    return normalBoardRepository.findAllByTypeAndSearch(boardType, search);
  }

  @Override
  @Transactional(readOnly = true)
  public NormalBoardDetailDto getPost(NormalBoardType boardType, Long boardId) {
    NormalBoard normalBoard =
        normalBoardRepository
            .findByTypeAndId(boardType, boardId)
            .orElseThrow(NotFoundException::new);

    ClassifiedFiles classifiedFiles = ClassifyFiles.classifyFiles(normalBoard.getFiles());

    return NormalBoardDetailDto.builder()
        .id(normalBoard.getId())
        .title(normalBoard.getTitle())
        .content(normalBoard.getContent())
        .writerId(normalBoard.getWriter().getId())
        .writerName(normalBoard.getWriter().getName())
        .datePinExpiration(normalBoard.getDatePinExpiration())
        .dateCreated(normalBoard.getDateCreated())
        .dateUpdated(normalBoard.getDateUpdated())
        .images(classifiedFiles.getImages())
        .otherFiles(classifiedFiles.getOtherFiles())
        .isPinned(normalBoard.getPinned())
        .build();
  }

  @Override
  @Transactional
  public Long write(
      Long memberId, NormalBoardType boardType, SaveNormalBoardDto saveNormalBoardDto) {

    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    Menu menu = menuRepository.findById(boardType.getMenuId()).orElseThrow(NotFoundException::new);

    NormalBoard normalBoard =
        new NormalBoard(
                saveNormalBoardDto.getTitle(), menu, saveNormalBoardDto.getContent(), false, null)
            .writtenBy(writer, NormalBoard.class);

    updateNormalBoardPinned(saveNormalBoardDto, boardType, normalBoard);
    List<String> fileIdList = saveNormalBoardDto.getFiles();
    List<BoardFile> boardFileList = boardFileRepository.getAllByIdInAndUploader(fileIdList, writer);
    normalBoard.updateFiles(boardFileList);

    for (BoardFile file : boardFileList) {
      file.toBoard(normalBoard);
    }

    return normalBoardRepository.save(normalBoard).getId();
  }

  @Override
  @Transactional
  public void update(
      Long boardId,
      NormalBoardType boardType,
      SaveNormalBoardDto saveNormalBoardDto,
      Long memberId) {
    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    NormalBoard normalBoard =
        normalBoardRepository.findById(boardId).orElseThrow(NotFoundException::new);
    normalBoard.updateText(saveNormalBoardDto.getTitle(), saveNormalBoardDto.getContent());
    updateNormalBoardPinned(saveNormalBoardDto, boardType, normalBoard);

    List<String> fileIdList = saveNormalBoardDto.getFiles();
    List<BoardFile> boardFileList = boardFileRepository.getAllByIdInAndUploader(fileIdList, writer);
    normalBoard.updateFiles(boardFileList);

    for (BoardFile file : boardFileList) {
      file.toBoard(normalBoard);
    }
  }

  @Override
  @Transactional
  public void delete(Long boardId) {
    normalBoardRepository.deleteById(boardId);
  }

  private void updateNormalBoardPinned(
      SaveNormalBoardDto saveNormalBoardDto, NormalBoardType boardType, NormalBoard normalBoard) {
    boolean isPinned = false;
    LocalDateTime datePinExpiration = null;

    if (hasPinned(boardType)) {
      if (saveNormalBoardDto.getPinOption() == null) {
        throw new InvalidInputException();
      } else if (saveNormalBoardDto.getPinOption().equals(TEMPORARY.getOption())) {
        isPinned = true;
        datePinExpiration = LocalDateTime.now().plusDays(TEMPORARY_DAYS);
      } else if (saveNormalBoardDto.getPinOption().equals(PERMANENT.getOption())) {
        isPinned = true;
        datePinExpiration = PERMANENT_DATE;
      }
    }

    normalBoard.updatePinned(isPinned, datePinExpiration);
  }

  private boolean hasPinned(NormalBoardType boardType) {
    return hasPinnedBoardTypeSet.contains(boardType);
  }
}
