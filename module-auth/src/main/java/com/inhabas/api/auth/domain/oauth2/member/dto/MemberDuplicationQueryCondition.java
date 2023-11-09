package com.inhabas.api.auth.domain.oauth2.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Phone;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.NoQueryParameterException;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.util.Objects;

@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDuplicationQueryCondition {

    private StudentId studentId;

    private Phone phoneNumber;

    public MemberDuplicationQueryCondition(StudentId studentId, String phoneNumber) {
        this.studentId = studentId;
        setPhoneNumber(phoneNumber);
    }

    public void verifyAtLeastOneParameter() {
        if (Objects.isNull(studentId) && Objects.isNull(phoneNumber)) {
            throw new NoQueryParameterException();
        }
    }

    public StudentId getStudentId() {
        return studentId;
    }

    /**
     * do not delete this method. this getter's return type is used for get parameter of SignUpController
     */
    public String getPhoneNumber() {
        return phoneNumber.getValue();
    }

    public void setStudentId(Integer memberId) {
        this.studentId = new StudentId(memberId);
    }

    public void setPhoneNumber(String phoneNumber) {
        if (Strings.isBlank(phoneNumber))
            this.phoneNumber = null;
        else
            this.phoneNumber = new Phone(phoneNumber);
    }

    public Phone getPhone() {
        return phoneNumber;
    }
}
