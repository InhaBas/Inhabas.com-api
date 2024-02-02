package com.inhabas.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class ApiApplication {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(ApiApplication.class);
    app.addListeners(new ApplicationPidFileWriter());
    app.run(args);
  }
}
