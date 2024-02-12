package com.inhabas.api.domain.board.usecase;

import javax.transaction.Transactional;

import com.inhabas.api.domain.board.domain.NormalBoardType;
import com.inhabas.api.domain.board.dto.BoardCountDto;
import com.inhabas.api.domain.board.dto.NormalBoardDetailDto;
import com.inhabas.api.domain.board.dto.SaveNormalBoardDto;
import com.inhabas.api.domain.board.exception.S3UploadFailedException;
import com.inhabas.api.domain.board.repository.BaseBoardRepository;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.file.dto.FileDownloadDto;
import com.inhabas.api.domain.file.usecase.S3Service;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.board.domain.NormalBoard;
import com.inhabas.api.domain.board.dto.NormalBoardDto;
import com.inhabas.api.domain.board.repository.NormalBoardRepository;
import com.inhabas.api.domain.menu.repository.MenuRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class NormalNormalBoardServiceImpl implements NormalBoardService {

  private final NormalBoardRepository normalBoardRepository;
  private final BaseBoardRepository baseBoardRepository;
  private final MenuRepository menuRepository;
  private final MemberRepository memberRepository;
  private final S3Service s3Service;


  @Override
  public List<BoardCountDto> getPostCount() {
    return baseBoardRepository.countRowsGroupByMenuId();
  }


  @Override
  public List<NormalBoardDto> getPosts(Long memberId, NormalBoardType boardType, String search) {
    List<NormalBoard> normalBoardList;
    if (boardType.equals(NormalBoardType.SUGGEST)) {
      normalBoardList = normalBoardRepository.findAllByMemberIdAndTypeAndSearch(memberId, boardType, search);
    } else {
      normalBoardList = normalBoardRepository.findAllByTypeAndSearch(boardType, search);
    }

    return normalBoardList.stream().map(board -> NormalBoardDto.builder()
            .id(board.getId())
            .title(board.getTitle())
            .isPinned(board.getPinned())
            .writerName(board.getWriter().getName())
            .dateCreated(board.getDateCreated())
            .dateUpdated(board.getDateUpdated())
            .build())
            .collect(Collectors.toList());
  }

  @Override
  public NormalBoardDetailDto getPost(Long memberId, NormalBoardType boardType, Long boardId) {
    NormalBoard normalBoard;
    List<FileDownloadDto> fileDownloadDtoList = null;
    if (boardType.equals(NormalBoardType.SUGGEST)) {
      normalBoard = normalBoardRepository.findMyByMemberIdAndTypeAndId(memberId, boardType, boardId);
    } else {
      normalBoard = normalBoardRepository.findByTypeAndId(boardType, boardId);
    }
    if (!normalBoard.getFiles().isEmpty()) {
      fileDownloadDtoList =
              normalBoard.getFiles().stream()
                      .map(obj -> FileDownloadDto.builder().name(obj.getName()).url(obj.getUrl()).build())
                      .collect(Collectors.toList());
    }


    return NormalBoardDetailDto.builder()
            .id(normalBoard.getId())
            .title(normalBoard.getTitle())
            .content(normalBoard.getContent())
            .writerName(normalBoard.getWriter().getName())
            .dateCreated(normalBoard.getDateCreated())
            .dateUpdated(normalBoard.getDateUpdated())
            .files(fileDownloadDtoList)
            .isPinned(normalBoard.getPinned())
            .build();
  }

  @Override
  public Long write(Long memberId, NormalBoardType normalBoardType, SaveNormalBoardDto saveNormalBoardDto) {

    Menu menu = menuRepository.findById(normalBoardType.getMenuId()).orElseThrow(NotFoundException::new);

    NormalBoard normalBoard =
            new NormalBoard(saveNormalBoardDto.getTitle(), menu, saveNormalBoardDto.getContent(), saveNormalBoardDto.getIsPinned());

    return normalBoardRepository.save(normalBoard).getId();
  }

  @Override
  public void update(Long boardId, NormalBoardType normalBoardType, SaveNormalBoardDto saveNormalBoardDto) {

    NormalBoard normalBoard =
        normalBoardRepository.findById(boardId).orElseThrow(NotFoundException::new);

    updateNormalBoardFiles(saveNormalBoardDto, normalBoardType, normalBoard);
  }

  @Override
  public void delete(Long boardId) {
    normalBoardRepository.deleteById(boardId);
  }


  private Long updateNormalBoardFiles(
          SaveNormalBoardDto saveNormalBoardDto, NormalBoardType normalBoardType, NormalBoard normalBoard) {
    final String DIR_NAME = "/" + normalBoardType.getBoardType();
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

}
