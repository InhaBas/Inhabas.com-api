package com.inhabas.api.auth.domain.oauth2.majorInfo.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.springframework.test.util.ReflectionTestUtils;

import com.inhabas.api.auth.domain.oauth2.majorInfo.domain.MajorInfo;
import com.inhabas.api.auth.domain.oauth2.majorInfo.dto.MajorInfoDto;
import com.inhabas.api.auth.domain.oauth2.majorInfo.dto.MajorInfoSaveDto;
import com.inhabas.api.auth.domain.oauth2.majorInfo.repository.MajorInfoRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class MajorInfoServiceTest {

  @InjectMocks private MajorInfoServiceImpl majorInfoService;

  @Mock private MajorInfoRepository majorInfoRepository;

  @DisplayName("모든 학과 정보를 불러온다.")
  @Test
  public void findAllTest() {

    // given
    MajorInfo majorInfo1 = new MajorInfo("공과대학", "기계공학과");
    MajorInfo majorInfo2 = new MajorInfo("자연과학대학", "수학과");
    MajorInfo majorInfo3 = new MajorInfo("경영대학", "경영학과");
    ReflectionTestUtils.setField(majorInfo1, "id", 1);
    ReflectionTestUtils.setField(majorInfo2, "id", 2);
    ReflectionTestUtils.setField(majorInfo3, "id", 3);
    List<MajorInfo> majorInfos =
        new ArrayList<>() {
          {
            add(majorInfo1);
            add(majorInfo2);
            add(majorInfo3);
          }
        };

    given(majorInfoRepository.findAll()).willReturn(majorInfos);

    // when
    List<MajorInfoDto> allMajorInfoDTOs = majorInfoService.getAllMajorInfo();

    // then
    then(majorInfoRepository).should(times(1)).findAll();
    assertThat(allMajorInfoDTOs)
        .hasSize(3)
        .extracting("id", "college", "major")
        .contains(tuple(1, "공과대학", "기계공학과"), tuple(2, "자연과학대학", "수학과"), tuple(3, "경영대학", "경영학과"));
  }

  @DisplayName("새로운 학과를 추가한다.")
  @Test
  public void saveMajorInfoTest() {

    // given
    MajorInfoSaveDto newMajor = new MajorInfoSaveDto("경영대학", "글로벌금융학과");
    given(majorInfoRepository.save(any(MajorInfo.class)))
        .willReturn(new MajorInfo("경영대학", "글로벌금융학과"));

    // when
    majorInfoService.saveMajorInfo(newMajor);

    // then
    then(majorInfoRepository).should(times(1)).save(any(MajorInfo.class));
  }

  @DisplayName("기존 전공 정보를 삭제한다.")
  @Test
  public void deleteMajorInfoTest() {

    // given
    Integer tempMajorInfoId = 1;
    doNothing().when(majorInfoRepository).deleteById(anyInt());

    // when
    majorInfoService.deleteMajorInfo(tempMajorInfoId);

    // then
    then(majorInfoRepository).should(times(1)).deleteById(anyInt());
  }
}
