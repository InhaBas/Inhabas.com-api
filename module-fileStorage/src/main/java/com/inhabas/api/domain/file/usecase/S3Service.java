package com.inhabas.api.domain.file.usecase;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

  String uploadS3File(MultipartFile multipartFile, String dirName);

  S3ObjectInputStream downloadS3File(String dirName);

  void deleteS3File(String fileUrl);
}
