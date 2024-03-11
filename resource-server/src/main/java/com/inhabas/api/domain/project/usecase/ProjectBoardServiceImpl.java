package com.inhabas.api.domain.project.usecase;

import static com.inhabas.api.domain.normalBoard.domain.PinOption.PERMANENT;
import static com.inhabas.api.domain.normalBoard.domain.PinOption.TEMPORARY;
import static com.inhabas.api.domain.project.ProjectBoardType.ALPHA;
import static com.inhabas.api.domain.project.ProjectBoardType.BETA;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

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
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDetailDto;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDto;
import com.inhabas.api.domain.normalBoard.dto.SaveNormalBoardDto;
import com.inhabas.api.domain.project.ProjectBoardType;
import com.inhabas.api.domain.project.repository.ProjectBoardRepository;
import com.inhabas.api.global.util.ClassifiedFiles;
import com.inhabas.api.global.util.ClassifyFiles;
import com.inhabas.api.global.util.FileUtil;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProjectBoardServiceImpl implements ProjectBoardService {

  private final ProjectBoardRepository projectBoardRepository;
  private final MenuRepository menuRepository;
  private final MemberRepository memberRepository;
  private final S3Service s3Service;

  private static final Set<ProjectBoardType> hasPinnedBoardTypeSet =
      new HashSet<>(Arrays.asList(ALPHA, BETA));
  private static final LocalDateTime PERMANENT_DATE = LocalDateTime.of(2200, 1, 1, 0, 0, 0);
  private static final Integer TEMPORARY_DAYS = 14;

  @Override
  public List<NormalBoardDto> getPinned(ProjectBoardType projectboardType) {
    List<NormalBoardDto> normalBoardList = new ArrayList<>();
    if (projectboardType.equals(ALPHA) || projectboardType.equals(BETA)) {
      normalBoardList = projectBoardRepository.findAllByTypeAndIsPinned(projectboardType);
    }
    return normalBoardList;
  }

  @Override
  public List<NormalBoardDto> getPosts(ProjectBoardType projectBoardType, String search) {
    List<NormalBoardDto> normalBoardList = new ArrayList<>();
    normalBoardList.addAll(projectBoardRepository.findAllByTypeAndSearch(projectBoardType, search));
    return normalBoardList;
  }

  @Override
  public NormalBoardDetailDto getPost(
      Long memberId, ProjectBoardType projectBoardType, Long boardId) {
    NormalBoard normalBoard;
    normalBoard =
        projectBoardRepository
            .findByTypeAndId(projectBoardType, boardId)
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
  public Long write(
      Long memberId, ProjectBoardType projectBoardType, SaveNormalBoardDto saveNormalBoardDto) {

    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    Menu menu =
        menuRepository.findById(projectBoardType.getMenuId()).orElseThrow(NotFoundException::new);

    NormalBoard normalBoard =
        new NormalBoard(
                saveNormalBoardDto.getTitle(), menu, saveNormalBoardDto.getContent(), false, null)
            .writtenBy(writer, NormalBoard.class);

    updateProjectBoardPinned(saveNormalBoardDto, projectBoardType, normalBoard);
    return updateProjectBoardFiles(saveNormalBoardDto, projectBoardType, normalBoard);
  }

  @Override
  public void update(
      Long boardId, ProjectBoardType projectBoardType, SaveNormalBoardDto saveNormalBoardDto) {

    NormalBoard normalBoard =
        projectBoardRepository.findById(boardId).orElseThrow(NotFoundException::new);
    updateProjectBoardPinned(saveNormalBoardDto, projectBoardType, normalBoard);
    updateProjectBoardFiles(saveNormalBoardDto, projectBoardType, normalBoard);
  }

  @Override
  public void delete(Long boardId) {
    projectBoardRepository.deleteById(boardId);
  }

  private Long updateProjectBoardFiles(
      SaveNormalBoardDto saveNormalBoardDto,
      ProjectBoardType projectBoardType,
      NormalBoard normalBoard) {
    final String DIR_NAME = projectBoardType.getBoardType() + "/";
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
    return projectBoardRepository.save(normalBoard).getId();
  }

  private void updateProjectBoardPinned(
      SaveNormalBoardDto saveNormalBoardDto,
      ProjectBoardType projectBoardType,
      NormalBoard normalBoard) {
    boolean isPinned = false;
    LocalDateTime datePinExpiration = null;

    if (hasPinned(projectBoardType)) {
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

  private boolean hasPinned(ProjectBoardType projectBoardType) {
    return hasPinnedBoardTypeSet.contains(projectBoardType);
  }
}
