package com.inhabas.api.domain.normalBoard.usecase;

import com.inhabas.api.auth.domain.error.authException.InvalidAuthorityException;
import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.board.exception.S3UploadFailedException;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.file.usecase.S3Service;
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
import com.inhabas.api.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.inhabas.api.domain.normalBoard.domain.NormalBoardType.*;
import static com.inhabas.api.domain.normalBoard.domain.NormalBoardType.EXECUTIVE;
import static com.inhabas.api.domain.normalBoard.domain.NormalBoardType.NOTICE;
import static com.inhabas.api.domain.normalBoard.domain.PinOption.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class NormalBoardServiceImpl implements NormalBoardService {

  private final NormalBoardRepository normalBoardRepository;
  private final MenuRepository menuRepository;
  private final MemberRepository memberRepository;
  private final S3Service s3Service;

  private static final Set<NormalBoardType> hasPinnedBoardTypeSet = new HashSet<>(
          Arrays.asList(NOTICE, EXECUTIVE));
  private static final LocalDateTime PERMANENT_DATE =
          LocalDateTime.of(2200, 1, 1, 0, 0, 0);
  private static final Integer TEMPORARY_DAYS = 14;

  @Override
  public List<NormalBoardDto> getPinned(NormalBoardType boardType) {
    List<NormalBoardDto> normalBoardList = new ArrayList<>();
    if (boardType.equals(NOTICE) || boardType.equals(EXECUTIVE)) {
      normalBoardList = normalBoardRepository.findAllByTypeAndIsPinned(boardType);
    }
    return normalBoardList;
  }

  @Override
  public List<NormalBoardDto> getPosts(NormalBoardType boardType, String search) {
    List<NormalBoardDto> normalBoardList = new ArrayList<>();
    if (boardType.equals(SUGGEST)) {
      if (SecurityContextHolder.getContext() == null) {
        throw new InvalidAuthorityException();
      }
      Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      normalBoardList.addAll(normalBoardRepository.findAllByMemberIdAndTypeAndSearch(memberId, boardType, search));
    } else {
      normalBoardList.addAll(normalBoardRepository.findAllByTypeAndSearch(boardType, search));
    }
    return normalBoardList;
  }

  @Override
  public NormalBoardDetailDto getPost(Long memberId, NormalBoardType boardType, Long boardId) {
    NormalBoard normalBoard;
    if (boardType.equals(SUGGEST)) {
      normalBoard = normalBoardRepository.findByMemberIdAndTypeAndId(memberId, boardType, boardId).orElseThrow(NotFoundException::new);
    } else {
      normalBoard = normalBoardRepository.findByTypeAndId(boardType, boardId).orElseThrow(NotFoundException::new);
    }

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
  public Long write(Long memberId, NormalBoardType boardType, SaveNormalBoardDto saveNormalBoardDto) {

    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    Menu menu = menuRepository.findById(boardType.getMenuId()).orElseThrow(NotFoundException::new);

    NormalBoard normalBoard = new NormalBoard(
            saveNormalBoardDto.getTitle(),
            menu,
            saveNormalBoardDto.getContent(),
            false,
            null
    ).writtenBy(writer, NormalBoard.class);

    updateNormalBoardPinned(saveNormalBoardDto, boardType, normalBoard);
    return updateNormalBoardFiles(saveNormalBoardDto, boardType, normalBoard);

  }


  @Override
  public void update(Long boardId, NormalBoardType boardType, SaveNormalBoardDto saveNormalBoardDto) {

    NormalBoard normalBoard =
        normalBoardRepository.findById(boardId).orElseThrow(NotFoundException::new);
    updateNormalBoardPinned(saveNormalBoardDto, boardType, normalBoard);
    updateNormalBoardFiles(saveNormalBoardDto, boardType, normalBoard);
  }

  @Override
  public void delete(Long boardId) {
    normalBoardRepository.deleteById(boardId);
  }


  private Long updateNormalBoardFiles(
          SaveNormalBoardDto saveNormalBoardDto, NormalBoardType boardType, NormalBoard normalBoard) {
    final String DIR_NAME = boardType.getBoardType() + "/";
    List<BoardFile> updateFiles = new ArrayList<>();
    List<String> urlListForDelete = new ArrayList<>();

    if (saveNormalBoardDto.getFiles() != null) {
      normalBoard.updateText(saveNormalBoardDto.getTitle(), saveNormalBoardDto.getContent());
      try {
        updateFiles =
                saveNormalBoardDto.getFiles().stream()
                        .map(
                                file -> {
                                  String path = FileUtil.generateFileName(file, DIR_NAME);
                                  String url = s3Service.uploadS3File(file, path);
                                  urlListForDelete.add(url);
                                  return BoardFile.builder()
                                          .name(file.getOriginalFilename())
                                          .url(url)
                                          .board(normalBoard)
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

    normalBoard.updateFiles(updateFiles);
    return normalBoardRepository.save(normalBoard).getId();
  }

  private void updateNormalBoardPinned(SaveNormalBoardDto saveNormalBoardDto, NormalBoardType boardType, NormalBoard normalBoard) {
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
