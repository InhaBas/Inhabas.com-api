package com.inhabas.api.domain.file.usecase;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service{

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client s3Client;

    @Override
    public String uploadS3File(MultipartFile multipartFile) {

        String fileName = multipartFile.getOriginalFilename();
        try {
            String contentType = getContentType(fileName);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);

            s3Client.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            e.printStackTrace();
        }

        printS3ObjectSummaries();

        return s3Client.getUrl(bucket, fileName).toString();
    }

    private String getContentType(String fileName) throws IOException {

        String ext = getExtension(fileName);
        Map<String, String> contentTypeMap = new HashMap<>();

        contentTypeMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        contentTypeMap.put("xls", "application/vnd.ms-excel");
        contentTypeMap.put("gif", "image/gif");
        contentTypeMap.put("jpeg", "image/jpeg");
        contentTypeMap.put("png", "image/png");
        contentTypeMap.put("doc", "application/msword");
        contentTypeMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        contentTypeMap.put("ppt", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        contentTypeMap.put("txt", "text/plain");
        contentTypeMap.put("pdf", "application/pdf");
        contentTypeMap.put("zip", "application/zip");
        contentTypeMap.put("csv", "text/csv");

        if (!contentTypeMap.containsKey(ext)) {
            throw new IOException();
        }
        return contentTypeMap.get(ext);

    }

    private void printS3ObjectSummaries() {
        ListObjectsV2Result listObjectsV2Result = s3Client.listObjectsV2(bucket);
        for (S3ObjectSummary object : listObjectsV2Result.getObjectSummaries()) {
            System.out.println("object = " + object.toString());
        }
    }

    private String getExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");

        if (lastIndex > 0 && lastIndex < fileName.length() - 1) {
            return fileName.substring(lastIndex + 1).toLowerCase();
        } else {
            return "";
        }
    }

}
