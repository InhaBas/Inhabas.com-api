package com.inhabas.api.global.util;

import java.util.List;

import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.file.dto.FileDownloadDto;

// 첨부파일을  썸네일, 이미지, (이미지가 아닌) 기타 파일로 분류
public class ClassifyFiles {

  public static ClassifiedFiles classifyFiles(List<BoardFile> files) {
    ClassifiedFiles result = new ClassifiedFiles();

    for (BoardFile file : files) {
      FileDownloadDto fileDto = new FileDownloadDto(file.getName(), file.getUrl());
      if (FileUtil.isImageFile(file.getName())) {
        result.addImage(fileDto);
        if (result.getThumbnail() == null) {
          result.setThumbnail(fileDto); // 첫 번째 이미지 파일을 썸네일로 선택
        }
      } else {
        result.addOtherFile(fileDto);
      }
    }

    return result;
  }
}
