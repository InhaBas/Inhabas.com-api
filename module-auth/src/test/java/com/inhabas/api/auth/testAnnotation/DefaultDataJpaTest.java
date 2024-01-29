package com.inhabas.api.auth.testAnnotation;

import com.inhabas.api.JpaConfig;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest
@ActiveProfiles("test")
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public @interface DefaultDataJpaTest {}
