package com.inhabas.api.domain.scholarship.usecase;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.file.repository.BoardFileRepository;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import com.inhabas.api.domain.scholarship.domain.Scholarship;
import com.inhabas.api.domain.scholarship.domain.ScholarshipBoardType;
import com.inhabas.api.domain.scholarship.dto.SaveScholarshipBoardDto;
import com.inhabas.api.domain.scholarship.dto.ScholarshipBoardDetailDto;
import com.inhabas.api.domain.scholarship.dto.ScholarshipBoardDto;
import com.inhabas.api.domain.scholarship.repository.ScholarshipBoardRepository;
import com.inhabas.api.global.util.ClassifiedFiles;
import com.inhabas.api.global.util.ClassifyFiles;

@Service
@Slf4j
@javax.transaction.Transactional
@RequiredArgsConstructor
public class ScholarshipBoardServiceImpl implements ScholarshipBoardService {

  private final BoardFileRepository boardFileRepository;

  private final ScholarshipBoardRepository scholarshipBoardRepository;
  private final MenuRepository menuRepository;
  private final MemberRepository memberRepository;

  @Transactional(readOnly = true)
  @Override
  public List<ScholarshipBoardDto> getPosts(ScholarshipBoardType boardType, String search) {
    List<ScholarshipBoardDto> scholarshipBoardDtoList = new ArrayList<>();
    scholarshipBoardDtoList.addAll(
        scholarshipBoardRepository.findAllByTypeAndSearch(boardType, search));
    return scholarshipBoardDtoList;
  }

  @Transactional(readOnly = true)
  @Override
  public ScholarshipBoardDetailDto getPost(
      ScholarshipBoardType boardType, Long boardId, Long memberId) {

    Scholarship scholarship =
        scholarshipBoardRepository
            .findByTypeAndId(boardType, boardId)
            .orElseThrow(NotFoundException::new);

    ClassifiedFiles classifiedFiles = ClassifyFiles.classifyFiles(scholarship.getFiles());

    return ScholarshipBoardDetailDto.builder()
        .id(scholarship.getId())
        .title(scholarship.getTitle())
        .content(scholarship.getContent())
        .writer(scholarship.getWriter())
        .dateCreated(scholarship.getDateCreated())
        .dateUpdated(scholarship.getDateUpdated())
        .images(classifiedFiles.getImages())
        .otherFiles(classifiedFiles.getOtherFiles())
        .build();
  }

  @Transactional
  @Override
  public Long write(
      ScholarshipBoardType boardType,
      SaveScholarshipBoardDto saveScholarshipBoardDto,
      Long memberId) {
    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    Menu menu = menuRepository.findById(boardType.getMenuId()).orElseThrow(NotFoundException::new);

    Scholarship scholarship =
        new Scholarship(
                saveScholarshipBoardDto.getTitle(),
                menu,
                saveScholarshipBoardDto.getContent(),
                saveScholarshipBoardDto.getDateHistory())
            .writtenBy(writer, Scholarship.class);

    List<String> fileIdList = saveScholarshipBoardDto.getFiles();
    List<BoardFile> boardFileList = boardFileRepository.getAllByIdInAndUploader(fileIdList, writer);
    scholarship.updateFiles(boardFileList);

    for (BoardFile file : boardFileList) {
      file.toBoard(scholarship);
    }

    return scholarshipBoardRepository.save(scholarship).getId();
  }

  @Transactional
  @Override
  public void update(
      Long boardId,
      ScholarshipBoardType boardType,
      SaveScholarshipBoardDto saveScholarshipBoardDto,
      Long memberId) {
    Scholarship scholarship =
        scholarshipBoardRepository
            .findByTypeAndId(boardType, boardId)
            .orElseThrow(NotFoundException::new);
    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

    scholarship.updateText(
        saveScholarshipBoardDto.getTitle(),
        saveScholarshipBoardDto.getContent(),
        saveScholarshipBoardDto.getDateHistory());

    List<String> fileIdList = saveScholarshipBoardDto.getFiles();
    List<BoardFile> boardFileList = boardFileRepository.getAllByIdInAndUploader(fileIdList, writer);
    scholarship.updateFiles(boardFileList);

    for (BoardFile file : boardFileList) {
      file.toBoard(scholarship);
    }
  }

  @Transactional
  @Override
  public void delete(ScholarshipBoardType boardType, Long boardId) {
    Scholarship scholarship =
        scholarshipBoardRepository
            .findByTypeAndId(boardType, boardId)
            .orElseThrow(NotFoundException::new);

    scholarshipBoardRepository.delete(scholarship);
  }
}
