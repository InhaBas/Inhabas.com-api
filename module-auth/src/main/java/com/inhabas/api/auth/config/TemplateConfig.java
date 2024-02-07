package com.inhabas.api.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class TemplateConfig {

  @Bean
  public SpringResourceTemplateResolver springResourceTemplateResolver() {
    SpringResourceTemplateResolver springResourceTemplateResolver =
        new SpringResourceTemplateResolver();
    springResourceTemplateResolver.setPrefix("classpath:/");
    springResourceTemplateResolver.setCharacterEncoding("UTF-8");
    springResourceTemplateResolver.setSuffix(".html");
    springResourceTemplateResolver.setTemplateMode(TemplateMode.HTML);
    springResourceTemplateResolver.setCacheable(false);

    return springResourceTemplateResolver;
  }

  @Bean
  public TemplateEngine htmlTemplateEngine(
      SpringResourceTemplateResolver springResourceTemplateResolver) {
    TemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.addTemplateResolver(springResourceTemplateResolver);

    return templateEngine;
  }
}
