package com.inhabas.api.domain.file.usecase;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public interface S3Service {

  String uploadS3File(MultipartFile multipartFile, String dirName)
      throws SdkClientException, IOException;

  String uploadS3Image(MultipartFile multipartFile, String fileName)
      throws SdkClientException, IOException;

  S3ObjectInputStream downloadS3File(String dirName);

  void deleteS3File(String fileUrl);
}
