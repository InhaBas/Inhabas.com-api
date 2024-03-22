package com.inhabas.api.domain.file.usecase;

import org.springframework.web.multipart.MultipartFile;

import com.inhabas.api.domain.file.dto.FileDownloadDto;

public interface BoardFileService {

  FileDownloadDto upload(Integer menuId, MultipartFile file, Long memberId);
}
