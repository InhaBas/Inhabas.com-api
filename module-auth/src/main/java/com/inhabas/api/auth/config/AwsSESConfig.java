package com.inhabas.api.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;

@Configuration
public class AwsSESConfig {

  @Value("${cloud.aws.ses.accessKey}")
  private String accessKey;

  @Value("${cloud.aws.ses.secretKey}")
  private String secretKey;

  @Value("${cloud.aws.region.static}")
  private String region;

  @Bean
  public AmazonSimpleEmailService amazonSimpleEmailService() {

    final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
    final AWSStaticCredentialsProvider awsStaticCredentialsProvider =
        new AWSStaticCredentialsProvider(basicAWSCredentials);

    return AmazonSimpleEmailServiceClientBuilder.standard()
        .withCredentials(awsStaticCredentialsProvider)
        .withRegion(region)
        .build();
  }
}
