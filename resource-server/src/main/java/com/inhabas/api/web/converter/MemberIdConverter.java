package com.inhabas.api.web.converter;

import com.inhabas.api.domain.member.MemberId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

public class MemberIdConverter {

    @Component
    public static class StringToEventConverter implements Converter<String, MemberId> {
        @Override
        public MemberId convert(String source){
            return new MemberId(Integer.parseInt(source));
        }
    }

    @Component
    public static class IntegerToEventConverter implements Converter<Integer, MemberId> {
        @Override
        public MemberId convert(Integer source){
            return new MemberId(source);
        }
    }

    @Component
    public static class EventToStringConverter implements Converter<MemberId, String>{
        @Override
        public String convert(MemberId source){
            return source.toString();
        }
    }

}
