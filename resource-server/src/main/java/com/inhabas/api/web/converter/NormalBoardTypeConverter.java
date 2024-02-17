package com.inhabas.api.web.converter;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.domain.normalBoard.domain.NormalBoardType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

public class NormalBoardTypeConverter {

    @Component
    public static class StringToContestTypeConverter implements Converter<String, NormalBoardType> {

        @Override
        public NormalBoardType convert(String source) {
            if (!StringUtils.hasText(source)) {
                throw new InvalidInputException();
            }
            try {
                return NormalBoardType.valueOf(source.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new NotFoundException();
            }
        }
    }

    @Component
    public static class ContestTypeToStringConverter implements Converter<NormalBoardType, String> {
        @Override
        public String convert(NormalBoardType source) {
            return source != null ? source.toString().toLowerCase() : null;
        }
    }
}