package com.inhabas.api.auth.domain.oauth2.member.security.masking;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JsonSerialize(using = StringPropertyMasker.class)
public @interface Masked {

  MaskingType type();
}
