package com.inhabas.api.domain.club.usecase;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.board.domain.AlbumBoard;
import com.inhabas.api.domain.club.dto.ClubActivityDetailDto;
import com.inhabas.api.domain.club.dto.ClubActivityDto;
import com.inhabas.api.domain.club.dto.SaveClubActivityDto;
import com.inhabas.api.domain.club.repository.ClubActivityRepository;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.file.dto.FileDownloadDto;
import com.inhabas.api.domain.file.repository.BoardFileRepository;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.repository.MenuRepository;

@Service
@RequiredArgsConstructor
public class ClubActivityServiceImpl implements ClubActivityService {

  private final ClubActivityRepository clubActivityRepository;

  private final BoardFileRepository boardFileRepository;

  private final MemberRepository memberRepository;

  private final MenuRepository menuRepository;

  private static final String CLUB_ACTIVITY_MENU_NAME = "동아리 활동";

  @Override
  @Transactional(readOnly = true)
  public List<ClubActivityDto> getClubActivities() {

    List<AlbumBoard> clubActivityList = clubActivityRepository.findAll();

    // 코드 수정 필요
    return clubActivityList.stream()
        .map(
            obj -> {
              String fileName = obj.getFiles().isEmpty() ? null : obj.getFiles().get(0).getName();
              String fileUrl = obj.getFiles().isEmpty() ? null : obj.getFiles().get(0).getUrl();
              return ClubActivityDto.builder()
                  .id(obj.getId())
                  .title(obj.getTitle())
                  .writerName(obj.getWriter().getName())
                  .dateCreated(obj.getDateCreated())
                  .dateUpdated(obj.getDateUpdated())
                  .thumbnail(
                      new FileDownloadDto(
                          obj.getFiles().get(0).getId(),
                          fileName,
                          fileUrl,
                          obj.getFiles().get(0).getSize(),
                          obj.getFiles().get(0).getType()))
                  .build();
            })
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public Long writeClubActivity(Long memberId, SaveClubActivityDto saveClubActivityDto) {

    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    Menu menu =
        menuRepository
            .findByName_Value(CLUB_ACTIVITY_MENU_NAME)
            .orElseThrow(NotFoundException::new);

    AlbumBoard clubActivity =
        AlbumBoard.builder()
            .menu(menu)
            .title(saveClubActivityDto.getTitle())
            .content(saveClubActivityDto.getContent())
            .build()
            .writtenBy(writer, AlbumBoard.class);

    List<String> fileIdList = saveClubActivityDto.getFiles();
    List<BoardFile> boardFileList = boardFileRepository.getAllByIdInAndUploader(fileIdList, writer);
    clubActivity.updateFiles(boardFileList);

    for (BoardFile file : boardFileList) {
      file.toBoard(clubActivity);
    }

    return clubActivityRepository.save(clubActivity).getId();
  }

  @Override
  @Transactional(readOnly = true)
  public ClubActivityDetailDto getClubActivity(Long boardId) {

    AlbumBoard clubActivity =
        clubActivityRepository.findById(boardId).orElseThrow(NotFoundException::new);
    List<FileDownloadDto> fileDownloadDtoList = null;
    if (!clubActivity.getFiles().isEmpty()) {
      fileDownloadDtoList =
          clubActivity.getFiles().stream()
              .map(obj -> FileDownloadDto.builder().name(obj.getName()).url(obj.getUrl()).build())
              .collect(Collectors.toList());
    }

    return ClubActivityDetailDto.builder()
        .id(clubActivity.getId())
        .title(clubActivity.getTitle())
        .content(clubActivity.getContent())
        .writerName(clubActivity.getWriter().getName())
        .dateCreated(clubActivity.getDateCreated())
        .dateUpdated(clubActivity.getDateUpdated())
        .files(fileDownloadDtoList)
        .build();
  }

  @Override
  @Transactional
  public void updateClubActivity(
      Long boardId, SaveClubActivityDto saveClubActivityDto, Long memberId) {
    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    AlbumBoard clubActivity =
        clubActivityRepository.findById(boardId).orElseThrow(NotFoundException::new);

    List<String> fileIdList = saveClubActivityDto.getFiles();
    List<BoardFile> boardFileList = boardFileRepository.getAllByIdInAndUploader(fileIdList, writer);
    clubActivity.updateFiles(boardFileList);

    for (BoardFile file : boardFileList) {
      file.toBoard(clubActivity);
    }
  }

  @Override
  @Transactional
  public void deleteClubActivity(Long boardId) {

    clubActivityRepository.deleteById(boardId);
  }
}
