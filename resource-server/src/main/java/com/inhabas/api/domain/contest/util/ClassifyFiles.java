package com.inhabas.api.domain.contest.util;

import java.util.List;

import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.file.dto.FileDownloadDto;

public class ClassifyFiles {

  public static ClassifiedFiles classifyFiles(List<BoardFile> files) {
    ClassifiedFiles result = new ClassifiedFiles();

    for (BoardFile file : files) {
      FileDownloadDto fileDto = new FileDownloadDto(file.getName(), file.getUrl());
      if (isImageFile(file.getName())) {
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

  private static boolean isImageFile(String fileName) {
    String lowerCaseFileName = fileName.toLowerCase();
    return lowerCaseFileName.endsWith(".jpg")
        || lowerCaseFileName.endsWith(".jpeg")
        || lowerCaseFileName.endsWith(".png")
        || lowerCaseFileName.endsWith(".gif")
        || lowerCaseFileName.endsWith(".bmp")
        || lowerCaseFileName.endsWith(".webp");
  }
}
