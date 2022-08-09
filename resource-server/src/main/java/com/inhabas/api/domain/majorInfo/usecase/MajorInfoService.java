package com.inhabas.api.domain.majorInfo.usecase;

import com.inhabas.api.domain.majorInfo.dto.MajorInfoDto;
import com.inhabas.api.domain.majorInfo.dto.MajorInfoSaveDto;

import java.util.List;

public interface MajorInfoService {

    List<MajorInfoDto> getAllMajorInfo();

    void saveMajorInfo(MajorInfoSaveDto majorInfoSaveDto);

    void deleteMajorInfo(Integer majorId);
}
