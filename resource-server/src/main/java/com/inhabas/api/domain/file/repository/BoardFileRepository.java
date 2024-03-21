package com.inhabas.api.domain.file.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.file.domain.BoardFile;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
  List<BoardFile> findAllByIdInAndUploader(List<String> fileIdList, Member uploader);

  List<BoardFile> getAllByIdInAndUploader(List<String> fileIdList, Member uploader);
}
