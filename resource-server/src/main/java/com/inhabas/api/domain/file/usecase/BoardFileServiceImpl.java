package com.inhabas.api.domain.file.usecase;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.board.exception.InvalidFileExtensionException;
import com.inhabas.api.domain.board.exception.S3UploadFailedException;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.file.dto.FileDownloadDto;
import com.inhabas.api.domain.file.repository.BoardFileRepository;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import com.inhabas.api.global.util.FileUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardFileServiceImpl implements BoardFileService {

  private final BoardFileRepository boardFileRepository;
  private final MenuRepository menuRepository;
  private final MemberRepository memberRepository;
  private final S3Service s3Service;

  @Transactional
  @Override
  public FileDownloadDto upload(Integer menuId, MultipartFile file, Long memberId) {

    Member uploader = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    Menu menu = menuRepository.findById(menuId).orElseThrow(NotFoundException::new);
    BoardFile boardFile;

    String id = FileUtil.generateUUID();
    String filePath = FileUtil.generateFilePathWithUUID(file, id, menu.getType().name());
    String url;
    try {
      url = s3Service.uploadS3File(file, filePath);
    } catch (IOException e) {
      throw new InvalidFileExtensionException();
    } catch (SdkClientException e) {
      throw new S3UploadFailedException();
    }
    boardFile =
        BoardFile.builder()
            .id(id)
            .name(file.getOriginalFilename())
            .url(url)
            .uploader(uploader)
            .size(file.getSize())
            .type(file.getContentType())
            .build();

    BoardFile savedBoardFile = boardFileRepository.save(boardFile);

    return FileDownloadDto.builder()
        .id(savedBoardFile.getId())
        .name(savedBoardFile.getName())
        .url(savedBoardFile.getUrl())
        .size(savedBoardFile.getSize())
        .type(savedBoardFile.getType())
        .build();
  }
}
