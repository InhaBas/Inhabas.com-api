package com.inhabas.api.global.util;

import java.util.List;

import com.inhabas.api.domain.file.domain.BaseFile;
import com.inhabas.api.domain.file.dto.FileDownloadDto;

// 첨부파일을  썸네일, 이미지, (이미지가 아닌) 기타 파일로 분류
public class ClassifyFiles {

  public static ClassifiedFiles classifyFiles(List<? extends BaseFile> files) {
    ClassifiedFiles result = new ClassifiedFiles();

    for (BaseFile file : files) {
      FileDownloadDto fileDto =
          new FileDownloadDto(
              file.getId(), file.getName(), file.getUrl(), file.getSize(), file.getType());
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
