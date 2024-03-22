package com.inhabas.api.domain.project.usecase;

import static com.inhabas.api.domain.board.domain.PinOption.PERMANENT;
import static com.inhabas.api.domain.board.domain.PinOption.TEMPORARY;
import static com.inhabas.api.domain.project.domain.ProjectBoardType.ALPHA;
import static com.inhabas.api.domain.project.domain.ProjectBoardType.BETA;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.inhabas.api.domain.project.domain.ProjectBoard;
import com.inhabas.api.domain.project.domain.ProjectBoardType;
import com.inhabas.api.domain.project.dto.ProjectBoardDetailDto;
import com.inhabas.api.domain.project.dto.ProjectBoardDto;
import com.inhabas.api.domain.project.dto.SaveProjectBoardDto;
import com.inhabas.api.domain.project.repository.ProjectBoardRepository;
import com.inhabas.api.global.util.ClassifiedFiles;
import com.inhabas.api.global.util.ClassifyFiles;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectBoardServiceImpl implements ProjectBoardService {

  private final ProjectBoardRepository projectBoardRepository;
  private final MenuRepository menuRepository;
  private final MemberRepository memberRepository;
  private final BoardFileRepository boardFileRepository;

  private static final Set<ProjectBoardType> hasPinnedBoardTypeSet =
      new HashSet<>(Arrays.asList(ALPHA, BETA));
  private static final LocalDateTime PERMANENT_DATE = LocalDateTime.of(2200, 1, 1, 0, 0, 0);
  private static final Integer TEMPORARY_DAYS = 14;

  @Override
  @Transactional
  public List<ProjectBoardDto> getPinned(ProjectBoardType projectboardType) {
    List<ProjectBoardDto> projectBoardList = new ArrayList<>();
    if (projectboardType.equals(ALPHA) || projectboardType.equals(BETA)) {
      projectBoardList = projectBoardRepository.findAllByTypeAndIsPinned(projectboardType);
    }
    return projectBoardList;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProjectBoardDto> getPosts(ProjectBoardType projectBoardType, String search) {
    List<ProjectBoardDto> projectBoardList = new ArrayList<>();
    projectBoardList.addAll(
        projectBoardRepository.findAllByTypeAndSearch(projectBoardType, search));
    return projectBoardList;
  }

  @Override
  @Transactional(readOnly = true)
  public ProjectBoardDetailDto getPost(
      Long memberId, ProjectBoardType projectBoardType, Long boardId) {
    ProjectBoard projectBoard;
    projectBoard =
        projectBoardRepository
            .findByTypeAndId(projectBoardType, boardId)
            .orElseThrow(NotFoundException::new);

    ClassifiedFiles classifiedFiles = ClassifyFiles.classifyFiles(projectBoard.getFiles());

    return ProjectBoardDetailDto.builder()
        .id(projectBoard.getId())
        .title(projectBoard.getTitle())
        .content(projectBoard.getContent())
        .writerId(projectBoard.getWriter().getId())
        .writerName(projectBoard.getWriter().getName())
        .datePinExpiration(projectBoard.getDatePinExpiration())
        .dateCreated(projectBoard.getDateCreated())
        .dateUpdated(projectBoard.getDateUpdated())
        .images(classifiedFiles.getImages())
        .otherFiles(classifiedFiles.getOtherFiles())
        .isPinned(projectBoard.getPinned())
        .build();
  }

  @Override
  @Transactional
  public Long write(
      Long memberId, ProjectBoardType projectBoardType, SaveProjectBoardDto saveProjectBoardDto) {

    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    Menu menu =
        menuRepository.findById(projectBoardType.getMenuId()).orElseThrow(NotFoundException::new);

    ProjectBoard projectBoard =
        new ProjectBoard(
                saveProjectBoardDto.getTitle(), menu, saveProjectBoardDto.getContent(), false, null)
            .writtenBy(writer, ProjectBoard.class);

    updateProjectBoardPinned(saveProjectBoardDto, projectBoardType, projectBoard);

    List<String> fileIdList = saveProjectBoardDto.getFiles();
    List<BoardFile> boardFileList = boardFileRepository.getAllByIdInAndUploader(fileIdList, writer);
    projectBoard.updateFiles(boardFileList);

    for (BoardFile file : boardFileList) {
      file.toBoard(projectBoard);
    }

    return projectBoardRepository.save(projectBoard).getId();
  }

  @Override
  @Transactional
  public void update(
      Long boardId,
      ProjectBoardType projectBoardType,
      SaveProjectBoardDto saveProjectBoardDto,
      Long memberId) {
    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    ProjectBoard projectBoard =
        projectBoardRepository.findById(boardId).orElseThrow(NotFoundException::new);
    projectBoard.updateText(saveProjectBoardDto.getTitle(), saveProjectBoardDto.getContent());
    updateProjectBoardPinned(saveProjectBoardDto, projectBoardType, projectBoard);

    List<String> fileIdList = saveProjectBoardDto.getFiles();
    List<BoardFile> boardFileList = boardFileRepository.getAllByIdInAndUploader(fileIdList, writer);
    projectBoard.updateFiles(boardFileList);

    for (BoardFile file : boardFileList) {
      file.toBoard(projectBoard);
    }
  }

  @Override
  @Transactional
  public void delete(Long boardId) {
    projectBoardRepository.deleteById(boardId);
  }

  private void updateProjectBoardPinned(
      SaveProjectBoardDto saveProjectBoardDto,
      ProjectBoardType projectBoardType,
      ProjectBoard projectBoard) {
    boolean isPinned = false;
    LocalDateTime datePinExpiration = null;

    if (hasPinned(projectBoardType)) {
      if (saveProjectBoardDto.getPinOption() == null) {
        throw new InvalidInputException();
      } else if (saveProjectBoardDto.getPinOption().equals(TEMPORARY.getOption())) {
        isPinned = true;
        datePinExpiration = LocalDateTime.now().plusDays(TEMPORARY_DAYS);
      } else if (saveProjectBoardDto.getPinOption().equals(PERMANENT.getOption())) {
        isPinned = true;
        datePinExpiration = PERMANENT_DATE;
      }
    }

    projectBoard.updatePinned(isPinned, datePinExpiration);
  }

  private boolean hasPinned(ProjectBoardType projectBoardType) {
    return hasPinnedBoardTypeSet.contains(projectBoardType);
  }
}
