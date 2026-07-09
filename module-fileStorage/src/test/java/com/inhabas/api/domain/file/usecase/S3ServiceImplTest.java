package com.inhabas.api.domain.file.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.io.IOException;
import java.net.URL;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class S3ServiceImplTest {

  private static final String BUCKET = "test-bucket";

  @InjectMocks S3ServiceImpl s3Service;
  @Mock AmazonS3Client s3Client;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(s3Service, "bucket", BUCKET);
  }

  private MockMultipartFile fileOf(String fileName) {
    return new MockMultipartFile("file", fileName, "application/octet-stream", "data".getBytes());
  }

  @DisplayName("허용된 확장자의 파일을 업로드하고 URL을 반환한다.")
  @Test
  void uploadS3FileTest() throws Exception {
    // given
    String fileName = "docs/test.pdf";
    given(s3Client.getUrl(BUCKET, fileName))
        .willReturn(new URL("https://test-bucket.s3.amazonaws.com/docs/test.pdf"));

    // when
    String url = s3Service.uploadS3File(fileOf("test.pdf"), fileName);

    // then
    assertThat(url).isEqualTo("https://test-bucket.s3.amazonaws.com/docs/test.pdf");
    ArgumentCaptor<PutObjectRequest> captor = ArgumentCaptor.forClass(PutObjectRequest.class);
    then(s3Client).should(times(1)).putObject(captor.capture());
    assertThat(captor.getValue().getBucketName()).isEqualTo(BUCKET);
    assertThat(captor.getValue().getKey()).isEqualTo(fileName);
    assertThat(captor.getValue().getMetadata().getContentType()).isEqualTo("application/pdf");
  }

  @DisplayName("허용되지 않은 확장자의 파일을 업로드하면 IOException을 던진다.")
  @Test
  void uploadS3FileInvalidExtensionTest() {
    assertThatThrownBy(() -> s3Service.uploadS3File(fileOf("malware.exe"), "docs/malware.exe"))
        .isInstanceOf(IOException.class);
  }

  @DisplayName("이미지 파일을 업로드하고 URL을 반환한다.")
  @Test
  void uploadS3ImageTest() throws Exception {
    // given
    String fileName = "images/test.png";
    given(s3Client.getUrl(BUCKET, fileName))
        .willReturn(new URL("https://test-bucket.s3.amazonaws.com/images/test.png"));

    // when
    String url = s3Service.uploadS3Image(fileOf("test.png"), fileName);

    // then
    assertThat(url).isEqualTo("https://test-bucket.s3.amazonaws.com/images/test.png");
    then(s3Client).should(times(1)).putObject(any(PutObjectRequest.class));
  }

  @DisplayName("이미지가 아닌 파일을 이미지로 업로드하면 IOException을 던진다.")
  @Test
  void uploadS3ImageInvalidExtensionTest() {
    assertThatThrownBy(() -> s3Service.uploadS3Image(fileOf("test.pdf"), "images/test.pdf"))
        .isInstanceOf(IOException.class);
  }

  @DisplayName("파일을 다운로드하면 S3 객체 스트림을 반환한다.")
  @Test
  void downloadS3FileTest() {
    // given
    S3Object s3Object = mock(S3Object.class);
    S3ObjectInputStream inputStream = mock(S3ObjectInputStream.class);
    given(s3Object.getObjectContent()).willReturn(inputStream);
    given(s3Client.getObject(any(GetObjectRequest.class))).willReturn(s3Object);

    // when
    S3ObjectInputStream result = s3Service.downloadS3File("docs/test.pdf");

    // then
    assertThat(result).isSameAs(inputStream);
  }

  @DisplayName("파일을 삭제하면 S3에 삭제 요청을 보낸다.")
  @Test
  void deleteS3FileTest() {
    // when
    s3Service.deleteS3File("docs/test.pdf");

    // then
    ArgumentCaptor<DeleteObjectRequest> captor = ArgumentCaptor.forClass(DeleteObjectRequest.class);
    then(s3Client).should(times(1)).deleteObject(captor.capture());
    assertThat(captor.getValue().getBucketName()).isEqualTo(BUCKET);
    assertThat(captor.getValue().getKey()).isEqualTo("docs/test.pdf");
  }
}
