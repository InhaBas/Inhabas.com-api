package com.inhabas.api.service.member;

import com.inhabas.api.dto.member.MajorInfoDto;
import com.inhabas.api.dto.member.MajorInfoSaveDto;

import java.util.List;

public interface MajorInfoService {

    List<MajorInfoDto> getAllMajorInfo();

    void saveMajorInfo(MajorInfoSaveDto majorInfoSaveDto);

    void deleteMajorInfo(Integer majorId);
}
