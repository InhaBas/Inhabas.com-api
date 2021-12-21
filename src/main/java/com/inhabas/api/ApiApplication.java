package com.inhabas.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

//	/**
//	 * The Class SwaggerConfig.
//	 */
//	@Configuration
//	public static class SwaggerConfig {
//		@Bean public Docket api() {
//
//			return new Docket(DocumentationType.OAS_30).select()
//					.apis(RequestHandlerSelectors.any())
//					.paths(PathSelectors.any()).build();
//		}
//	}
}


