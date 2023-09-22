package com.inhabas.api.domain.member.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpgradeRequest {
    private List<Integer> memberId;

}
