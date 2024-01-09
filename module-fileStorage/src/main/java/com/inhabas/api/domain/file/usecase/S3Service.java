package com.inhabas.api.domain.file.usecase;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

    String uploadS3File(MultipartFile multipartFile);

}
