package com.inhabas.api.domain.majorInfo.usecase;

import com.inhabas.api.domain.majorInfo.domain.MajorInfo;
import com.inhabas.api.domain.majorInfo.repository.MajorInfoRepository;
import com.inhabas.api.domain.majorInfo.dto.MajorInfoDto;
import com.inhabas.api.domain.majorInfo.dto.MajorInfoSaveDto;
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
