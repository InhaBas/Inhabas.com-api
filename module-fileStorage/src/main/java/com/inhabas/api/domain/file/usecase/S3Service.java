package com.inhabas.api.domain.file.usecase;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3ObjectInputStream;

public interface S3Service {

  String uploadS3File(MultipartFile multipartFile, String dirName);

  String uploadS3Image(MultipartFile multipartFile, String fileName);

  S3ObjectInputStream downloadS3File(String dirName);

  void deleteS3File(String fileUrl);
}
