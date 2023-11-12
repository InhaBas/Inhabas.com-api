package com.inhabas.api.web.converter;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

public class MemberIdConverter {

    @Component
    public static class StringToMemberIdConverter implements Converter<String, StudentId> {
        @Override
        public StudentId convert(String source){
            return new StudentId(Integer.parseInt(source));
        }
    }

    @Component
    public static class IntegerToMemberIdConverter implements Converter<Integer, StudentId> {
        @Override
        public StudentId convert(Integer source){
            return new StudentId(source);
        }
    }

    @Component
    public static class MemberIdToStringConverter implements Converter<StudentId, String>{
        @Override
        public String convert(StudentId source){
            return source.toString();
        }
    }

}
