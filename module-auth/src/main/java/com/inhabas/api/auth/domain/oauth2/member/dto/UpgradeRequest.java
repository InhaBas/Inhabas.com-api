package com.inhabas.api.auth.domain.oauth2.member.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpgradeRequest {
    private List<Integer> memberId;

}
