package com.inhabas.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
@Profile({"dev", "local", "test", "integration_test"})
public class S3Config {

  @Value("${cloud.aws.credentials.accessKey}")
  private String accessKey;

  @Value("${cloud.aws.credentials.secretKey}")
  private String secretKey;

  @Value("${cloud.aws.region.static}")
  private String region;

  @Bean
  @Primary
  public AmazonS3Client amazonS3Client() {
    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    return (AmazonS3Client)
        AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
            .build();
  }
}
