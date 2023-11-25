package com.inhabas.api.auth.domain.oauth2.majorInfo.usecase;

import com.inhabas.api.auth.domain.oauth2.majorInfo.domain.MajorInfo;
import com.inhabas.api.auth.domain.oauth2.majorInfo.repository.MajorInfoRepository;
import com.inhabas.api.auth.domain.oauth2.majorInfo.dto.MajorInfoDto;
import com.inhabas.api.auth.domain.oauth2.majorInfo.dto.MajorInfoSaveDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MajorInfoServiceImpl implements MajorInfoService {

    private final MajorInfoRepository majorInfoRepository;

    public List<MajorInfoDto> getAllMajorInfo() {
        return majorInfoRepository.findAll().stream()
                .map(majorInfo -> new MajorInfoDto(majorInfo.getId(), majorInfo.getCollege(), majorInfo.getMajor()))
                .collect(Collectors.toList());
    }

    public void saveMajorInfo(MajorInfoSaveDto majorInfoSaveDto) {
        majorInfoRepository.save(
                new MajorInfo(majorInfoSaveDto.getCollege(), majorInfoSaveDto.getMajor())
        );
    }

    public void deleteMajorInfo(Integer majorId) {
        majorInfoRepository.deleteById(majorId);
    }
}
