package com.inhabas.api.web.argumentResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Parameter;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(hidden = true)
public @interface Authenticated {}
