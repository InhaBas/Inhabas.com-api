package com.inhabas.api.auth.domain.oauth2.member.repository;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.MemberTest;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.UpdateNameRequest;
import com.inhabas.api.auth.testAnnotation.DefaultDataJpaTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultDataJpaTest
class UpdateNameRequestRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    UpdateNameRequestRepository updateNameRequestRepository;

    @DisplayName("객체 하나를 저장한다.")
    @Test
    @Transactional
    public void saveTest() {
        //given
        Member member = MemberTest.basicMember();
        Member savedMember = memberRepository.save(member);

        UpdateNameRequest updateNameRequest = UpdateNameRequest.builder()
                .member(savedMember)
                .name("조변경")
                .build();

        //when
        UpdateNameRequest result = updateNameRequestRepository.save(updateNameRequest);

        //then
        assertThat(result.getName()).isEqualTo(updateNameRequest.getName());
        assertThat(result.getMember().getId()).isEqualTo(savedMember.getId());

    }

}