package com.inhabas.api.auth.config;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {

  private final OAuth2 oauth2 = new OAuth2();

  public static final class OAuth2 {

    private String defaultRedirectUri;

    private final List<String> authorizedRedirectUris = new ArrayList<>();

    public String getDefaultRedirectUri() {
      return defaultRedirectUri;
    }

    public void setDefaultRedirectUri(String defaultRedirectUri) {
      this.defaultRedirectUri = defaultRedirectUri;
    }

    public List<String> getAuthorizedRedirectUris() {
      return authorizedRedirectUris;
    }

    /**
     * OAuth2 인증 요청 시 redirect_uri 를 변조하려는 불법적인 시도를 막기 위한 보안적인 조치
     *
     * @param uri 프론트에서 입력한 redirect_uri
     * @return api 에 등록되어 있는 uri 이면 true.
     */
    public boolean isAuthorizedRedirectUri(String uri) {
      URI clientRedirectUri = URI.create(uri);
      return this.authorizedRedirectUris.stream()
          .anyMatch(
              authorizedRedirectUri -> {
                // scheme, host, port 가 동일해야 함.
                URI authorizedURI = URI.create(authorizedRedirectUri);
                return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                    && authorizedURI.getPort() == clientRedirectUri.getPort()
                    && authorizedURI.getScheme().equalsIgnoreCase(clientRedirectUri.getScheme());
              });
    }
  }

  public OAuth2 getOauth2() {
    return oauth2;
  }
}
