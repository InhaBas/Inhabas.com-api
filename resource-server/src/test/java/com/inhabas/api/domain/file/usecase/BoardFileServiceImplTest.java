package com.inhabas.api.domain.file.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Optional;

import org.springframework.mock.web.MockMultipartFile;

import com.amazonaws.SdkClientException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.board.exception.InvalidFileExtensionException;
import com.inhabas.api.domain.board.exception.S3UploadFailedException;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.file.dto.FileDownloadDto;
import com.inhabas.api.domain.file.repository.BoardFileRepository;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.MenuGroup;
import com.inhabas.api.domain.menu.domain.valueObject.MenuType;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class BoardFileServiceImplTest {

  @InjectMocks BoardFileServiceImpl boardFileService;
  @Mock BoardFileRepository boardFileRepository;
  @Mock MenuRepository menuRepository;
  @Mock MemberRepository memberRepository;
  @Mock S3Service s3Service;

  private static final Integer MENU_ID = 1;
  private static final Long MEMBER_ID = 1L;

  private MockMultipartFile validFile() {
    return new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());
  }

  @DisplayName("파일을 업로드하면 FileDownloadDto를 반환한다.")
  @Test
  void uploadTest_Success() throws Exception {
    // given
    Member member = MemberTest.chiefMember();
    Menu menu = new Menu(mock(MenuGroup.class), 1, MenuType.LIST, "공지사항", "공지사항 게시판");
    MockMultipartFile file = validFile();

    given(memberRepository.findById(MEMBER_ID)).willReturn(Optional.of(member));
    given(menuRepository.findById(MENU_ID)).willReturn(Optional.of(menu));
    given(s3Service.uploadS3File(any(), anyString())).willReturn("https://s3.url/test.txt");
    given(boardFileRepository.save(any(BoardFile.class)))
        .willAnswer(invocation -> invocation.getArgument(0));

    // when
    FileDownloadDto fileDownloadDto = boardFileService.upload(MENU_ID, file, MEMBER_ID);

    // then
    assertThat(fileDownloadDto.getName()).isEqualTo(file.getOriginalFilename());
    assertThat(fileDownloadDto.getUrl()).isEqualTo("https://s3.url/test.txt");
    assertThat(fileDownloadDto.getSize()).isEqualTo(file.getSize());
    assertThat(fileDownloadDto.getType()).isEqualTo(file.getContentType());
    assertThat(fileDownloadDto.getId()).isNotBlank();
  }

  @DisplayName("존재하지 않는 회원이 업로드하면 MemberNotFoundException을 던진다.")
  @Test
  void uploadTest_MemberNotFound() {
    // given
    given(memberRepository.findById(MEMBER_ID)).willReturn(Optional.empty());

    // when then
    assertThatThrownBy(() -> boardFileService.upload(MENU_ID, validFile(), MEMBER_ID))
        .isInstanceOf(MemberNotFoundException.class);
  }

  @DisplayName("존재하지 않는 메뉴에 업로드하면 NotFoundException을 던진다.")
  @Test
  void uploadTest_MenuNotFound() {
    // given
    given(memberRepository.findById(MEMBER_ID)).willReturn(Optional.of(MemberTest.chiefMember()));
    given(menuRepository.findById(MENU_ID)).willReturn(Optional.empty());

    // when then
    assertThatThrownBy(() -> boardFileService.upload(MENU_ID, validFile(), MEMBER_ID))
        .isInstanceOf(NotFoundException.class);
  }

  @DisplayName("허용되지 않는 확장자면 InvalidFileExtensionException을 던진다.")
  @Test
  void uploadTest_InvalidFileExtension() throws Exception {
    // given
    Menu menu = new Menu(mock(MenuGroup.class), 1, MenuType.LIST, "공지사항", "공지사항 게시판");
    given(memberRepository.findById(MEMBER_ID)).willReturn(Optional.of(MemberTest.chiefMember()));
    given(menuRepository.findById(MENU_ID)).willReturn(Optional.of(menu));
    given(s3Service.uploadS3File(any(), anyString())).willThrow(new IOException());

    // when then
    assertThatThrownBy(() -> boardFileService.upload(MENU_ID, validFile(), MEMBER_ID))
        .isInstanceOf(InvalidFileExtensionException.class);
  }

  @DisplayName("S3 업로드에 실패하면 S3UploadFailedException을 던진다.")
  @Test
  void uploadTest_S3UploadFailed() throws Exception {
    // given
    Menu menu = new Menu(mock(MenuGroup.class), 1, MenuType.LIST, "공지사항", "공지사항 게시판");
    given(memberRepository.findById(MEMBER_ID)).willReturn(Optional.of(MemberTest.chiefMember()));
    given(menuRepository.findById(MENU_ID)).willReturn(Optional.of(menu));
    given(s3Service.uploadS3File(any(), anyString())).willThrow(new SdkClientException("fail"));

    // when then
    assertThatThrownBy(() -> boardFileService.upload(MENU_ID, validFile(), MEMBER_ID))
        .isInstanceOf(S3UploadFailedException.class);
  }
}
