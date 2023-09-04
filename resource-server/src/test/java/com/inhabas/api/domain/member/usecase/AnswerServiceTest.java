package com.inhabas.api.domain.member.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.inhabas.api.domain.member.domain.MemberTest;
import com.inhabas.api.domain.member.domain.entity.Answer;
import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.repository.AnswerRepository;
import com.inhabas.api.domain.member.repository.MemberRepository;
import com.inhabas.api.domain.member.dto.AnswerDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AnswerServiceTest {

    @InjectMocks
    private AnswerServiceImpl answerService;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private MemberRepository memberRepository;


    @DisplayName("사용자가 작성한 답변이 엔티티로 잘 변환되는지 확인한다.")
    @Test
    public void saveAnswersTest() {
        //given
        Member member = MemberTest.MEMBER1();
        MemberId currentUserId = member.getId();
        given(memberRepository.getById(any())).willReturn(member);

        ArrayList<AnswerDto> submittedAnswers = new ArrayList<>() {{
            add(new AnswerDto(1, "저는 꼭 이 동아리에 입부하고 싶습니다."));
            add(new AnswerDto(2, "어렸을적부터 빅데이터를 발가락으로 전처리하며 놀았습니다."));
            add(new AnswerDto(3, "외주를 받아 진행했던 적이 있는데, 아주 잘 되어 스타트업 창업을 진행했습니다."));
            add(new AnswerDto(4, "이 동아리에 입부한다면, 말하는 대로 코딩해주는 인공지능 모델을 개발하고 싶습니다."));
        }};

        ArrayList<Answer> expectedConvertedEntityList = new ArrayList<>() {{
            add(new Answer(member, 1, "저는 꼭 이 동아리에 입부하고 싶습니다."));
            add(new Answer(member, 2, "어렸을적부터 빅데이터를 발가락으로 전처리하며 놀았습니다."));
            add(new Answer(member, 3, "외주를 받아 진행했던 적이 있는데, 아주 잘 되어 스타트업 창업을 진행했습니다."));
            add(new Answer(member, 4, "이 동아리에 입부한다면, 말하는 대로 코딩해주는 인공지능 모델을 개발하고 싶습니다."));
        }};
        given(answerRepository.saveAll(any())).willReturn(expectedConvertedEntityList);

        //when
        answerService.saveAnswers(submittedAnswers, currentUserId);

        //then
        ArgumentCaptor<ArrayList<Answer>> argumentCaptor = ArgumentCaptor.forClass(ArrayList.class);
        verify(answerRepository, times(1)).saveAll(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(expectedConvertedEntityList);
    }


    @DisplayName("사용자가 저장한 답변을 dto로 변환해서 반환한다.")
    @Test
    public void loadAnswersTest() {
        //given
        Member member = MemberTest.MEMBER1();
        MemberId currentUserId = member.getId();

        ArrayList<Answer> savedAnswers = new ArrayList<>() {{
            add(new Answer(member, 1, "저는 꼭 이 동아리에 입부하고 싶습니다."));
            add(new Answer(member, 2, "어렸을적부터 빅데이터를 발가락으로 전처리하며 놀았습니다."));
            add(new Answer(member, 3, "외주를 받아 진행했던 적이 있는데, 아주 잘 되어 스타트업 창업을 진행했습니다."));
            add(new Answer(member, 4, "이 동아리에 입부한다면, 말하는 대로 코딩해주는 인공지능 모델을 개발하고 싶습니다."));
        }};
        given(answerRepository.findByMember_id(any())).willReturn(savedAnswers);

        ArrayList<AnswerDto> expectedConvertedDTOs = new ArrayList<>() {{
            add(new AnswerDto(1, "저는 꼭 이 동아리에 입부하고 싶습니다."));
            add(new AnswerDto(2, "어렸을적부터 빅데이터를 발가락으로 전처리하며 놀았습니다."));
            add(new AnswerDto(3, "외주를 받아 진행했던 적이 있는데, 아주 잘 되어 스타트업 창업을 진행했습니다."));
            add(new AnswerDto(4, "이 동아리에 입부한다면, 말하는 대로 코딩해주는 인공지능 모델을 개발하고 싶습니다."));
        }};

        //when
        List<AnswerDto> returnedDTOs = answerService.getAnswers(currentUserId);

        //then
        assertThat(returnedDTOs)
                .usingRecursiveComparison()
                .isEqualTo(expectedConvertedDTOs);
    }

    @DisplayName("특정 회원이 작성한 답변이 있는지 확인한다.")
    @Test
    public void existAnswersWrittenByMemberTest() {
        //given
        given(answerRepository.existsByMember_id(any())).willReturn(true);

        //when
        boolean result = answerService.existAnswersWrittenBy(new MemberId(12171652));

        //then
        assertTrue(result);
        then(answerRepository).should(times(1)).existsByMember_id(any());
    }
}
