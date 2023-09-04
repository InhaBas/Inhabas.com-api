package com.inhabas.api.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.domain.valueObject.Phone;
import com.inhabas.api.domain.member.NoQueryParameterException;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.util.Objects;

@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDuplicationQueryCondition {

    private MemberId memberId;

    private Phone phoneNumber;

    public MemberDuplicationQueryCondition(MemberId memberId, String phoneNumber) {
        this.memberId = memberId;
        setPhoneNumber(phoneNumber);
    }

    public void verifyAtLeastOneParameter() {
        if (Objects.isNull(memberId) && Objects.isNull(phoneNumber)) {
            throw new NoQueryParameterException();
        }
    }

    public MemberId getMemberId() {
        return memberId;
    }

    /**
     * do not delete this method. this getter's return type is used for get parameter of SignUpController
     */
    public String getPhoneNumber() {
        return phoneNumber.getValue();
    }

    public void setMemberId(Integer memberId) {
        this.memberId = new MemberId(memberId);
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
