package com.inhabas.api.auth.domain.oauth2.majorInfo.usecase;

import com.inhabas.api.auth.domain.oauth2.majorInfo.dto.MajorInfoDto;
import com.inhabas.api.auth.domain.oauth2.majorInfo.dto.MajorInfoSaveDto;
import java.util.List;

public interface MajorInfoService {

  List<MajorInfoDto> getAllMajorInfo();

  void saveMajorInfo(MajorInfoSaveDto majorInfoSaveDto);

  void deleteMajorInfo(Integer majorId);
}
