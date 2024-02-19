package com.inhabas.api.global.util;

import java.util.ArrayList;
import java.util.List;

import com.inhabas.api.domain.file.dto.FileDownloadDto;

// ClassifyFiles 에 의해 분류된 파일들이 ClassifiedFiles 형태로 반환됨.
public class ClassifiedFiles {
  private FileDownloadDto thumbnail;
  private List<FileDownloadDto> images = new ArrayList<>();
  private List<FileDownloadDto> otherFiles = new ArrayList<>();

  public ClassifiedFiles() {}

  public FileDownloadDto getThumbnail() {
    return thumbnail;
  }

  public List<FileDownloadDto> getImages() {
    return images;
  }

  public List<FileDownloadDto> getOtherFiles() {
    return otherFiles;
  }

  public void setThumbnail(FileDownloadDto thumbnail) {
    this.thumbnail = thumbnail;
  }

  public void addImage(FileDownloadDto image) {
    this.images.add(image);
  }

  public void addOtherFile(FileDownloadDto file) {
    this.otherFiles.add(file);
  }
}
