package com.inhabas.api.web;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EnvironmentController {

  private final Environment environment;

  @GetMapping("/prod/profiles")
  public String prodProfile() {
    final List<String> profiles = Arrays.asList(environment.getActiveProfiles());
    final List<String> prodProfiles = Arrays.asList("production1", "production2");
    final String defaultProfile = profiles.get(0);

    return Arrays.stream(environment.getActiveProfiles())
        .filter(prodProfiles::contains)
        .findAny()
        .orElse(defaultProfile);
  }

  @GetMapping("/dev/profiles")
  public String devProfile() {
    final List<String> profiles = Arrays.asList(environment.getActiveProfiles());
    final List<String> prodProfiles = Arrays.asList("dev1", "dev2");
    final String defaultProfile = profiles.get(0);

    return Arrays.stream(environment.getActiveProfiles())
        .filter(prodProfiles::contains)
        .findAny()
        .orElse(defaultProfile);
  }
}
