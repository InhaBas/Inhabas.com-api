package com.inhabas.testConfig;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles({"production"})
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public @interface CustomSpringBootTest {

    @AliasFor(annotation = SpringBootTest.class, attribute = "classes")
    Class<?>[] classes() default {};
}
