package com.inhabas.api.auth.domain.oauth2.member.security.masking;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Masking {

    public static String mask(MaskingType type, String value) {
        if (type == null) return value;

        String str;
        switch (type) {
            case PHONE:
                str = phoneMaskOf(value);
                break;
            case EMAIL:
                str = emailMaskOf(value);
                break;
            default:
                str = value;
                break;
        }
        return str;
    }

    private static String phoneMaskOf(String value){

        // 총무 권한이 있는 유저만 접근 가능
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean hasAnyRole = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals("ROLE_SECRETARY") || auth.equals("ROLE_CHIEF") || auth.equals("ROLE_VICE_CHIEF"));

        if (!hasAnyRole) {
            // 010-****-****
            String regex = "(\\d{3})-?(\\d{4})-?(\\d{4})$";
            Matcher matcher = Pattern.compile(regex).matcher(value);
            if (matcher.find()) {
                return matcher.group(1) + "-****-****";
            }
        }

        return value;
    }


    private static String emailMaskOf(String value){
        // abc****@gmail.com
        return value.replaceAll("(?<=.{3}).(?=[^@]*?@)", "*");
    }
}