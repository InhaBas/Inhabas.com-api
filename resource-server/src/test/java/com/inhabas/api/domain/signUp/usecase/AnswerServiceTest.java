package com.inhabas.api.domain.signUp.usecase;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.questionnaire.domain.Questionnaire;
import com.inhabas.api.domain.questionnaire.repository.QuestionnaireRepository;
import com.inhabas.api.domain.signUp.domain.entity.Answer;
import com.inhabas.api.domain.signUp.dto.AnswerDto;
import com.inhabas.api.domain.signUp.repository.AnswerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.inhabas.api.domain.member.domain.entity.MemberTest.signingUpMemberAfterProfile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AnswerServiceTest {

    @InjectMocks
    private AnswerServiceImpl answerService;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private QuestionnaireRepository questionnaireRepository;

    private static final ArrayList<Questionnaire> questionnaires = new ArrayList<>() {{
        add(new Questionnaire(1L, "질문 1"));
        add(new Questionnaire(2L, "질문 2"));
        add(new Questionnaire(3L, "질문 3"));
    }};

    private static final Member member = signingUpMemberAfterProfile();

    private static final ArrayList<Answer> savedAnswers = new ArrayList<>() {{
        add(new Answer(member, questionnaires.get(0), "저는 꼭 이 동아리에 입부하고 싶습니다."));
        add(new Answer(member, questionnaires.get(1), "어렸을적부터 빅데이터를 발가락으로 전처리하며 놀았습니다."));
        add(new Answer(member, questionnaires.get(2), "외주를 받아 진행했던 적이 있는데, 아주 잘 되어 스타트업 창업을 진행했습니다."));
    }};

    private static final ArrayList<AnswerDto> submittedAnswers = new ArrayList<>() {{
        add(new AnswerDto(1L, "저는 꼭 이 동아리에 입부하고 싶습니다."));
        add(new AnswerDto(2L, "어렸을적부터 빅데이터를 발가락으로 전처리하며 놀았습니다."));
        add(new AnswerDto(3L, "외주를 받아 진행했던 적이 있는데, 아주 잘 되어 스타트업 창업을 진행했습니다."));
    }};

    @DisplayName("사용자가 작성한 답변이 엔티티로 잘 변환되는지 확인한다.")
    @Test
    public void saveAnswersTest() {
        //given
        given(memberRepository.findById(any())).willReturn(Optional.of(member));
        given(answerRepository.findByMember_Id(any())).willReturn(savedAnswers);
        given(questionnaireRepository.findAll()).willReturn(questionnaires);
        given(questionnaireRepository.countByIdIn(any())).willReturn(3L);
        given(questionnaireRepository.findById(1L)).willReturn(Optional.ofNullable(questionnaires.get(0)));
        given(questionnaireRepository.findById(2L)).willReturn(Optional.ofNullable(questionnaires.get(1)));
        given(questionnaireRepository.findById(3L)).willReturn(Optional.ofNullable(questionnaires.get(2)));

        //when
        answerService.saveAnswers(submittedAnswers, member.getId());

        //then
        ArgumentCaptor<ArrayList<Answer>> argumentCaptor = ArgumentCaptor.forClass(ArrayList.class);
        verify(answerRepository, times(1)).saveAll(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue())
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(savedAnswers);
    }


    @DisplayName("사용자가 저장한 답변을 dto로 변환해서 반환한다.")
    @Test
    public void getAnswersTest() {
        //given
        given(memberRepository.findById(any())).willReturn(Optional.of(member));
        given(questionnaireRepository.findAll()).willReturn(questionnaires);
        given(answerRepository.findByMember_Id(any())).willReturn(savedAnswers);

        //when
        List<AnswerDto> returnedDTOs = answerService.getAnswers(member.getId());

        //then
        assertThat(returnedDTOs)
                .usingRecursiveComparison()
                .isEqualTo(submittedAnswers);

    }

}
