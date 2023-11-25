package com.inhabas.api.web.configTest;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.inhabas.api.config.WebConfig;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;
import com.inhabas.testConfig.TestEmptyController;
import io.swagger.v3.core.jackson.ModelResolver;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@NoSecureWebMvcTest(TestEmptyController.class)
@Import(WebConfig.class)
public class WebConfigTest {

    @Autowired
    private ModelResolver modelResolver;

    @DisplayName("swagger 명세로 변환하는 modelResolver 는 LOWER_CAMEL_CASE 를 따른다.")
    @Test
    public void ModelResolverSnakeCaseTest() {
        //when
        PropertyNamingStrategy propertyNamingStrategy = modelResolver.objectMapper()
                .getPropertyNamingStrategy();

        //then
        Assertions.assertThat(propertyNamingStrategy)
                .isEqualTo(PropertyNamingStrategies.LOWER_CAMEL_CASE);
    }
}
