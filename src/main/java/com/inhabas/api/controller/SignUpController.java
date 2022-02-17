package com.inhabas.api.controller;

import com.inhabas.api.domain.member.type.wrapper.Phone;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.dto.member.MajorInfoDto;
import com.inhabas.api.dto.signUp.AnswerDto;
import com.inhabas.api.dto.signUp.DetailSignUpDto;
import com.inhabas.api.dto.signUp.QuestionnaireDto;
import com.inhabas.api.dto.signUp.StudentSignUpDto;
import com.inhabas.api.security.argumentResolver.Authenticated;
import com.inhabas.api.security.domain.AuthUserDetail;
import com.inhabas.api.security.domain.AuthUserService;
import com.inhabas.api.service.member.MajorInfoService;
import com.inhabas.api.service.member.MemberService;
import com.inhabas.api.service.questionnaire.AnswerService;
import com.inhabas.api.service.questionnaire.QuestionnaireService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private final MemberService memberService;
    private final AnswerService answerService;
    private final AuthUserService authUserService;
    private final MajorInfoService majorInfoService;
    private final QuestionnaireService questionnaireService;

    /* profile */

    @PostMapping("/signUp/student")
    @Operation(description = "회원가입 시 프로필을 저장한다.")
    public ResponseEntity<?> saveProfile(@Authenticated AuthUserDetail authUser, @Valid @RequestBody StudentSignUpDto form) {
        memberService.saveSignUpForm(form);
        authUserService.setProfileIdToSocialAccount(authUser.getId(), form.getStudentId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/signUp/student")
    @Operation(description = "임시저장한 개인정보를 불러온다.")
    public ResponseEntity<DetailSignUpDto> loadProfile(@Authenticated AuthUserDetail signUpUser) {
        DetailSignUpDto form = memberService.loadSignUpForm(signUpUser.getProfileId(), signUpUser.getEmail());

        return ResponseEntity.ok(form);
    }

    @GetMapping("/signUp/majorInfo")
    @Operation(description = "회원가입에 필요한 전공 정보를 모두 불러온다.")
    public ResponseEntity<List<MajorInfoDto>> loadAllMajorInfo() {
        return ResponseEntity.ok(majorInfoService.getAllMajorInfo());
    }

    @GetMapping("/signUp/isDuplicated")
    @Operation(description = "회원가입 시 필요한 중복검사를 진행한다.")
    public ResponseEntity<?> validateDuplication(
            @RequestParam(required = false) Integer memberId,
            @RequestParam(required = false) Phone phone) {

        /* 학번, 핸드폰번호 동시에 넘어오거나, 하나도 안들어오는경우 */
        if (Objects.isNull(memberId) && Objects.isNull(phone)
                || Objects.nonNull(memberId) && Objects.nonNull(phone)) {
            return ResponseEntity.badRequest().build();
        }
        else {
            boolean isDuplicated;

            if (Objects.nonNull(memberId)) {
                isDuplicated = memberService.isDuplicatedId(memberId);
            }
            else {
                isDuplicated = memberService.isDuplicatedPhoneNumber(phone);
            }

            return ResponseEntity.ok(isDuplicated);
        }
    }

    /* questionnaire */

    @GetMapping("/signUp/questionnaire")
    @Operation(description = "회원가입에 필요한 질문들을 불러온다.")
    public ResponseEntity<List<QuestionnaireDto>> loadQuestionnaire() {

        return ResponseEntity.ok(questionnaireService.getQuestionnaire());
    }

    /* answer */

    @GetMapping("/signUp/answer")
    @Operation(description = "회원가입 도중 임시 저장한 질문지 답변을 불러온다.")
    public ResponseEntity<List<AnswerDto>> loadAnswers(@Authenticated AuthUserDetail signUpUser) {
        List<AnswerDto> answers = answerService.getAnswers(signUpUser.getProfileId());

        return ResponseEntity.ok(answers);
    }

    @PostMapping("/signUp/answer")
    @Operation(description = "회원가입 시 작성한 질문을 저장한다.")
    public ResponseEntity<?> saveAnswers(
            @Authenticated AuthUserDetail signUpUser, @Valid @RequestBody List<AnswerDto> answers) {
        answerService.saveAnswers(answers, signUpUser.getProfileId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /* finish signUp */
    @PutMapping("/signUp/finish")
    @Operation(description = "회원가입을 완료한다")
    public ResponseEntity<?> finishSignUp(@Authenticated AuthUserDetail signUpUser) {
        authUserService.finishSignUp(signUpUser.getId());
        memberService.changeRole(signUpUser.getProfileId(), Role.NOT_APPROVED_MEMBER);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
